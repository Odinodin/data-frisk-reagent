# data-frisk-reagent

"Get your facts first, then you can distort them as you please" - Mark Twain

Visualize your atom data in your Reagent apps as a tree structure.

Suitable for use during development.

## Install

Add `[data-frisk-reagent "0.1.3"]` to the dev `:dependencies` in your `project.clj`

## Usage

```clojure
(ns datafrisk.demo
  (:require [reagent.core :as r]
            [datafrisk.core :as d]))

(def store (r/atom {:data {:some-string "a"
                           :vector-with-map [1 2 3 3 {:a "a" :b "b"}]
                           :a-set #{1 2 3}
                           :a-map {:x "x" :y "y" :z [1 2 3 4]}
                           :a-list '(1 2 3)
                           :a-seq (seq [1 2])
                           :an-object (clj->js {:a "a"})
                           :this-is-a-very-long-keyword :g}

                    :data-frisk {:expanded-paths #{[] [:data]}
                                 :visible? true}}))

(r/render
    [d/DataFriskShell store]
    (js/document.getElementById "app"))
```

See the dev/demo.cljs namespace for usage

## License

Copyright © 2016 Odin Standal

Distributed under the MIT License (MIT)