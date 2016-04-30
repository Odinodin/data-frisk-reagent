(ns datadrill.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(def store (r/atom {:data {:a "a"
                           :b [1 2 3 3 {:a "a" :b "b"}]
                           :c #{1 2 3}
                           :d {:x "x" :y "y" :z [1 2 3 4]}
                           :e '(1 2 3)}
                    :data-drill {:expansion #{}}}))

(declare DataDrill)

(defn ExpandButton [{:keys [expanded? path emit-fn]}]
  (if expanded?
    [:button {:onClick #(emit-fn :contract path)} "-"]
    [:button {:onClick #(emit-fn :expand path)} "+"]))

(defn CollapseAllButton [emit-fn]
  [:button {:onClick #(emit-fn :collapse-all)} "Collapse all"])

(defn Node [{:keys [data path]}]
  [:div (cond
          (string? data)
          (str "'" data "'")

          (keyword? data)
          (str data)
          :else
          data)])

(defn KeyValNode [{[k v] :data path :path expansion :expansion emit-fn :emit-fn}]
  [:div {:style {:display "flex"}}
   [:div {:style {:flex 0 :padding "2px"}}
    (str k)]
   [:div {:style {:flex 1 :padding "2px"}}
    [DataDrill {:data v
                :path (conj path k)
                :expansion expansion
                :emit-fn emit-fn}]]])

(defn ListVecNode [{:keys [data path expansion emit-fn]}]
  (let [expanded? (get expansion path)]
    [:div {:style {:display "flex"}}
     [:div {:style {:flex 0}} [ExpandButton {:expanded? expanded?
                                             :path path
                                             :emit-fn emit-fn}]]
     [:div {:style {:flex 1}} [:span (if (list? data) "(" "[")]
      (if expanded?
        (map-indexed (fn [i x] ^{:key i} [DataDrill {:data x
                                                     :path (conj path i)
                                                     :expansion expansion
                                                     :emit-fn emit-fn}]) data)
        (str (count data) " items"))
      [:span (if (list? data) ")" "]")]]]))

(defn SetNode [{:keys [data path expansion emit-fn]}]
  (let [expanded? (get expansion path)]
    [:div {:style {:display "flex"}}
     [:div {:style {:flex 0}} [ExpandButton {:expanded? expanded?
                                             :path path
                                             :emit-fn emit-fn}]]
     [:div {:style {:flex 1}} [:span "#{"]
      (if expanded?
        (map-indexed (fn [i x] ^{:key i} [DataDrill {:data x
                                                     :path (conj path x)
                                                     :expansion expansion
                                                     :emit-fn emit-fn}]) data)
        (str (count data) " items"))
      [:span "}"]]]))

(defn MapNode [{:keys [data path expansion emit-fn]}]
  (let [expanded? (get expansion path)]
    [:div {:style {:display "flex"}}
     [:div {:style {:flex 0}}
      [ExpandButton {:expanded? expanded? :path path :emit-fn emit-fn}]]
     [:div {:style {:flex 1}}
      [:span "{"]
      (if expanded?
        (map-indexed (fn [i x] ^{:key i} [KeyValNode {:data x :path path :expansion expansion :emit-fn emit-fn}]) data)
        (clojure.string/join " " (keys data)))
      [:span "}"]]]))

(defn DataDrill [{:keys [data] :as all}]
  (cond (map? data) [MapNode all]
        (set? data) [SetNode all]
        (or (vector? data) (list? data)) [ListVecNode all]
        :else [Node all]))

(defn conj-to-set [coll x]
  (conj (or coll #{}) x))

(defn emit-fn-factory [data-atom]
  (fn [event & args]
    (prn "Emit: " event args)
    (case event
      :expand (swap! data-atom update-in [:data-drill :expansion] conj-to-set (first args))
      :contract (swap! data-atom update-in [:data-drill :expansion] disj (first args))
      :collapse-all (swap! data-atom assoc-in [:data-drill :expansion] #{}))))

(defn Root [data-atom]
  (let [data-drill (:data-drill @data-atom)
        emit-fn (emit-fn-factory data-atom)
        raw (dissoc @data-atom :data-drill)]
    [:div
     [:div (str data-drill)]
     [CollapseAllButton emit-fn]
     [DataDrill {:data raw
                 :path []
                 :expansion (:expansion data-drill)
                 :emit-fn emit-fn}]]))

(defn DataDrillShellVisibleButton [visible? toggle-visible-fn]
  (if visible?
    [:div {:style {:overflow "hidden"}}
     [:div {:onClick toggle-visible-fn
            :style {:backgroundColor "#4EE24E"
                    :padding "12px"
                    :float "right"
                    :width "80px"
                    :text-align "center"}}
      "Hide"]]
    [:div {:onClick toggle-visible-fn
           :style {:backgroundColor "#4EE24E"
                   :padding "12px"
                   :position "fixed"
                   :bottom 0
                   :right 0
                   :width "80px"
                   :text-align "center"}}
     "Data drill"]))

(defn DataDrillShell [data-atom]
  (let [data-drill (:data-drill @data-atom)
        visible? (:visible? data-drill)]
    [:div {:style {:backgroundColor "#EEFFED"
                   :position "fixed"
                   :right 0
                   :bottom 0
                   :width "100%"
                   :height "50%"
                   :max-height (if visible? "50%" 0)
                   :transition "all 0.3s ease-out"
                   :padding 0}}
     [DataDrillShellVisibleButton visible? (fn [_] (swap! data-atom assoc-in [:data-drill :visible?] (not visible?)))]
     [:div {:style {:padding "10px"
                    :height "100%"
                    :overflow-y "scroll"}}
      [Root data-atom]]]))

(defn mount-root []
  (r/render
    [DataDrillShell store]
    (js/document.getElementById "app")))

(defn ^:export main []
  (mount-root))

(defn on-js-reload []
  (mount-root))