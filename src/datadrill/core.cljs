(ns datadrill.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(def store (r/atom {:data {:a "a"
                           :b [1 2 3 3 {:a "a" :b "b"}]
                           :c #{1 2 3}
                           :d {:x "x" :y "y" :z [1 2 3 4]}
                           :e '(1 2 3)}
                    :data-drill {:expansion #{}}}))

(defn emit [event & args]
  (prn "Emit: " event args)
  (case event
    :expand (swap! store update-in [:data-drill :expansion] conj (first args))
    :contract (swap! store update-in [:data-drill :expansion] disj (first args))
    :collapse-all (swap! store assoc-in [:data-drill :expansion] #{})))

(declare DataDrill)

(defn ExpandButton [{:keys [expanded? path]}]
  (if expanded?
    [:button {:onClick #(emit :contract path)} "-"]
    [:button {:onClick #(emit :expand path)} "+"]))

(defn CollapseAllButton []
  [:button {:onClick #(emit :collapse-all)} "Collapse all"])

(defn Node [{:keys [data path]}]
  [:div (cond
          (string? data)
          (str "'" data "'")

          (keyword? data)
          (str data)
          :else
          data)])

(defn KeyValNode [{[k v] :data path :path expansion :expansion}]
  [:div {:style {:display "flex"}}
   [:div {:style {:flex 0 :padding "2px"}}
    (str k)]
   [:div {:style {:flex 1 :padding "2px"}}
    [DataDrill {:data v
                :path (conj path k)
                :expansion expansion}]]])

(defn ListVecNode [{:keys [data path expansion]}]
  (let [expanded? (get expansion path)]
    [:div
     [ExpandButton {:expanded? expanded? :path path}]
     [:span (if (list? data) "(" "[")]
     (if expanded?
       (map-indexed (fn [i x] ^{:key i} [DataDrill {:data x :path (conj path i) :expansion expansion}]) data)
       (str (count data) " items"))
     [:span (if (list? data) ")" "]")]]))

(defn SetNode [{:keys [data path expansion]}]
  (let [expanded? (get expansion path)]
    [:div
     [ExpandButton {:expanded? expanded? :path path}]
     [:span "#{"]
     (if expanded?
       (map-indexed (fn [i x] ^{:key i} [DataDrill {:data x :path (conj path x)} :expansion expansion]) data)
       (str (count data) " items")
       )
     [:span "}"]]))

(defn MapNode [{:keys [data path expansion]}]
  (let [expanded? (get expansion path)]
    [:div
     [ExpandButton {:expanded? expanded? :path path}]
     [:span "{"]
     (if expanded?
       (map-indexed (fn [i x] ^{:key i} [KeyValNode {:data x :path path :expansion expansion}]) data)
       (clojure.string/join " " (keys data)))
     [:span "}"]]))

(defn DataDrill [{:keys [data] :as all}]
  (cond (map? data) [MapNode all]
        (set? data) [SetNode all]
        (or (vector? data) (list? data)) [ListVecNode all]
        :else [Node all]))

(defn Root [data-atom]
  (let [data-drill (:data-drill @data-atom)
        raw (dissoc @data-atom :data-drill)]
    [:div
     [:div (str data-drill)]
     [CollapseAllButton]
     [DataDrill {:data raw
                 :path []
                 :expansion (:expansion data-drill)}]]))

(defn mount-root []
  (r/render
    [Root store]
    (js/document.getElementById "app")))

(defn ^:export main []
  (mount-root))

(defn on-js-reload []
  (mount-root))