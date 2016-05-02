(ns datafrisk.demo
  (:require [reagent.core :as r]
            [datafrisk.core :as d]))

(enable-console-print!)

(def store (r/atom {:data {:a "a"
                           :b [1 2 3 3 {:a "a" :b "b"}]
                           :c #{1 2 3}
                           :d {:x "x" :y "y" :z [1 2 3 4]}
                           :e '(1 2 3)
                           :f (clj->js {:a "a"})
                           :this-is-a-very-long-keyword :g}

                    :data-frisk {:expansion #{[] [:data]}
                                 :visible? true}}))

(defn mount-root []
  (r/render
    [d/DataFriskShell store]
    (js/document.getElementById "app")))

(defn ^:export main []
  (mount-root))

(defn on-js-reload []
  (mount-root))