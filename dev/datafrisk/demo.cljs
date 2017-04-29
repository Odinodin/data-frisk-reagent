(ns datafrisk.demo
  (:require [reagent.core :as r]
            [datafrisk.core :as d]))

(enable-console-print!)

(defn Animals [data]
  [:div "Awesome animals"
   (into [:ul]
     (map-indexed (fn [i {:keys [animal age]}]
                    ^{:key i} [:li (str animal ", " age " years old")])
       (:animals data)))])

(def state (r/atom {:animals '({:animal "Monkey", :age 22222}
                                    {:animal "Giraffe", :age 45}
                                    {:animal "Zebra" :age 3})
                        :some-string "a"
                        :vector-with-map [1 2 3 3 {:a "a" :b "b"}]
                        :a-set #{1 2 3}
                        :a-map {:x "x" :y "y" :z [1 2 3 4]}
                        :atom (atom {:x "x" :y "y" :z [1 2 3 4]})
                        :a-seq (seq [1 2])
                        :an-object (clj->js {:a "a"})
                        :this-is-a-very-long-keyword :g}))

(defn App [state]
  (let [state @state]
    [:div
     [Animals state]
     [d/DataFriskShell
      ;; List of arguments you want to visualize
      state
      {:a :b :c :d :e :f}]]))

(defn mount-root []
  (r/render
    [App state]
    (js/document.getElementById "app")))

(defn ^:export main []
  (mount-root))

(defn on-js-reload []
  (mount-root))