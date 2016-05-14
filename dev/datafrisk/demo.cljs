(ns datafrisk.demo
  (:require [reagent.core :as r]
            [datafrisk.core :as d]))

(enable-console-print!)

(def store (r/atom {:data {:some-string "a"
                           :vector-with-map [1 2 3 3 {:a "a" :b "b"}]
                           :a-set #{1 2 3}
                           :a-map {:x "x" :y "y" :z [1 2 3 4]}
                           :a-list '(1 2 3)
                           :a-seq (seq [1 2])
                           :an-object (clj->js {:a "a"})
                           :this-is-a-very-long-keyword :g}
                    :data-frisk {:visible? true}}))

(defn mount-root []
  (r/render
    [d/DataFriskShell store (:data @store) {:a :b :c :d}]
    (js/document.getElementById "app")))

(defn ^:export main []
  (mount-root))

(defn on-js-reload []
  (mount-root))