(ns datadrill.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(def store (r/atom {:data {:a "a"
                         :b [1 2 3 3 {:a "a" :b "b"}]
                         :c "c"
                         :d {:x "x" :y "y" :z [1 2 3 4]}}
                  :expansion #{}}))

(defn emit [event args]
  (prn "Emit: " event args)
  (case event
    :expand (swap! store update :expansion conj args)
    :contract (swap! store update :expansion disj args)))

(declare DataDrill)

(defn Node [{:keys [data path]}]
  [:div (if (string? data)
          (str "'" data "'")
          data)])

(defn KeyValNode [{[k v] :data path :path expansion :expansion}]
  [:div {:style {:display "flex"}}
   [:div {:style {:flex 0 :padding "2px"}}
    (str k)]
   [:div {:style {:flex 1 :padding "2px"}}
    [DataDrill {:data v
                :path (conj path k)
                :expansion expansion}]]])

(defn ExpandButton [{:keys [expanded? path]}]
  (if expanded?
    [:button {:onClick #(emit :contract path)} "-"]
    [:button {:onClick #(emit :expand path)} "+"]))

(defn VecNode [{:keys [data path expansion]}]
  (let [expanded? (get expansion path)]
    [:div
     [ExpandButton {:expanded? expanded? :path path}]
     [:span "["]
     (if expanded?
       (map-indexed (fn [i x] ^{:key i} [DataDrill {:data x :path (conj path i) :expansion expansion}]) data)
       (str (count data))
       )
     [:span "]"]]))

(defn MapNode [{:keys [data path expansion]}]
  (prn "Mapnode: " data "path: " path "expan: " expansion)
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
        (vector? data) [VecNode all]
        :else [Node all]))

(defn App []
  (let [{:keys [data expansion]} @store]
    [:div
     (str expansion)
     [DataDrill {:data data
                 :path []
                 :expansion expansion}]]))

(defn mount-root []
  (r/render
    [App]
    (js/document.getElementById "app")))

(defn ^:export main []
  (mount-root))

(defn on-js-reload []
  (mount-root))