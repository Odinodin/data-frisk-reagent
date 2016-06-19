(ns datafrisk.core
  (:require [reagent.core :as r]))

(declare DataFrisk)

(defn ExpandButton [{:keys [expanded? path emit-fn]}]
  [:button {:style {:border "0"
                    :backgroundColor "transparent" :width "20px" :height "20px"}
            :onClick #(emit-fn (if expanded? :contract :expand) path)}
   [:svg {:viewBox "0 0 100 100"
          :width "100%" :height "100%"
          :style {:transition "all 0.2s ease"
                  :transform (when expanded? "rotate(90deg)")}}
    [:polygon {:points "0,0 0,100 100,50" :stroke "black"}]]])

(def styles
  {:shell {:backgroundColor "#FAFAFA"}
   :strings {:color "#4Ebb4E"}
   :keywords {:color "purple"}
   :numbers {:color "blue"}
   :shell-visible-button {:backgroundColor "#4EE24E"}})

(defn CollapseAllButton [emit-fn]
  [:button {:onClick #(emit-fn :collapse-all)
            :style {:padding "7px"
                    :cursor "pointer"
                    :border 1
                    :backgroundColor "lightgray"}}
   "Collapse all"])

(defn Node [{:keys [data path]}]
  [:div (cond
          (string? data)
          [:span {:style (:strings styles)} (str "\"" data "\"")]

          (keyword? data)
          [:span {:style (:keywords styles)} (str data)]

          (object? data)
          (str data " " (.stringify js/JSON data))

          (number? data)
          [:span {:style (:numbers styles)} data]

          :else
          (str data))])

(defn KeyValNode [{[k v] :data path :path expanded-paths :expanded-paths emit-fn :emit-fn}]
  [:div {:style {:display "flex"}}
   [:div {:style {:flex "0 0 auto" :padding "2px"}}
    [:span {:style (:keywords styles)} (str k)]]
   [:div {:style {:flex "1" :padding "2px"}}
    [DataFrisk {:data v
                :path (conj path k)
                :expanded-paths expanded-paths
                :emit-fn emit-fn}]]])

(defn ListVecNode [{:keys [data path expanded-paths emit-fn]}]
  (let [expanded? (get expanded-paths path)]
    [:div {:style {:display "flex"}}
     (when-not (empty? data)
       [:div {:style {:flex "0 1 auto"}} [ExpandButton {:expanded? expanded?
                                               :path path
                                               :emit-fn emit-fn}]])
     [:div {:style {:flex 1}}
      [:span (if (vector? data) "[" "(")]
      (if expanded?
        (map-indexed (fn [i x] ^{:key i} [DataFrisk {:data x
                                                     :path (conj path i)
                                                     :expanded-paths expanded-paths
                                                     :emit-fn emit-fn}]) data)
        (str (count data) " items"))
      [:span (if (vector? data) "]" ")")]]]))

(defn SetNode [{:keys [data path expanded-paths emit-fn]}]
  (let [expanded? (get expanded-paths path)]
    [:div {:style {:display "flex"}}
     (when-not (empty? data)
       [:div {:style {:flex "0 1 auto"}}
        [ExpandButton {:expanded? expanded?
                       :path path
                       :emit-fn emit-fn}]])
     [:div {:style {:flex 1}} [:span "#{"]
      (if expanded?
        (map-indexed (fn [i x] ^{:key i} [DataFrisk {:data x
                                                     :path (conj path x)
                                                     :expanded-paths expanded-paths
                                                     :emit-fn emit-fn}]) data)
        (str (count data) " items"))
      [:span "}"]]]))

(defn MapNode [{:keys [data path expanded-paths emit-fn]}]
  (let [expanded? (get expanded-paths path)]
    [:div {:style {:display "flex"}}
     [:div {:style {:flex "0 1 auto"}}
      [ExpandButton {:expanded? expanded? :path path :emit-fn emit-fn}]]
     [:div {:style {:flex 1}}
      [:span "{"]
      (if expanded?
        (map-indexed (fn [i x] ^{:key i} [KeyValNode {:data x :path path :expanded-paths expanded-paths :emit-fn emit-fn}]) data)
        [:span {:style (:keywords styles)} (clojure.string/join " " (keys data))])
      [:span "}"]]]))

(defn DataFrisk [{:keys [data] :as all}]
  (cond (map? data) [MapNode all]
        (set? data) [SetNode all]
        (or (seq? data) (vector? data)) [ListVecNode all]
        (satisfies? IDeref data) [DataFrisk (assoc all :data @data)]
        :else [Node all]))

(defn conj-to-set [coll x]
  (conj (or coll #{}) x))

(defn emit-fn-factory [data-atom id]
  (fn [event & args]
    (prn "Emit: " id event args)
    (case event
      :expand (swap! data-atom update-in [:data-frisk id :expanded-paths] conj-to-set (first args))
      :contract (swap! data-atom update-in [:data-frisk id :expanded-paths] disj (first args))
      :collapse-all (swap! data-atom assoc-in [:data-frisk id :expanded-paths] #{}))))

(defn Root [data id state-atom]
  (let [data-frisk (:data-frisk @state-atom)
        emit-fn (emit-fn-factory state-atom id)]
    [:div
     [CollapseAllButton emit-fn]
     [DataFrisk {:data data
                 :path []
                 :expanded-paths (get-in data-frisk [id :expanded-paths])
                 :emit-fn emit-fn}]]))

(defn DataFriskShellVisibleButton [visible? toggle-visible-fn]
  [:button {:onClick toggle-visible-fn
         :style (merge {:border 0
                        :cursor "pointer"
                        :font "inherit"
                        :padding "12px"
                        :position "fixed"
                        :right 0
                        :width "80px"
                        :text-align "center"}
                  (:shell-visible-button styles)
                  (when-not visible? {:bottom 0}))}
   (if visible? "Hide" "Data frisk")])

(defn DataFriskShell [& data]
  (let [expand-by-default (reduce #(assoc-in %1 [:data-frisk %2 :expanded-paths] #{[]}) {} (range (count data)))
        state-atom (r/atom expand-by-default)]
    (fn [& data]
      (let [data-frisk (:data-frisk @state-atom)
            visible? (:visible? data-frisk)]
        [:div {:style (merge {:position "fixed"
                              :fontFamily "Consolas,Monaco,Courier New,monospace"
                              :fontSize "12px"
                              :right 0
                              :bottom 0
                              :width "100%"
                              :height "50%"
                              :max-height (if visible? "50%" 0)
                              :transition "all 0.3s ease-out"
                              :padding 0}
                        (:shell styles))}
         [DataFriskShellVisibleButton visible? (fn [_] (swap! state-atom assoc-in [:data-frisk :visible?] (not visible?)))]
         [:div {:style {:padding "10px"
                        :height "100%"
                        :box-sizing "border-box"
                        :overflow-y "scroll"}}
          (map-indexed (fn [id x]
                         ^{:key id} [Root x id state-atom]) data)]]))))