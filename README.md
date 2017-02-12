# data-frisk-reagent

> "Get your facts first, then you can distort them as you please" - Mark Twain

Visualize your data in your Reagent apps as a tree structure.

Suitable for use during development.

<img src="data-frisk.gif">

## Install

![](https://clojars.org/data-frisk-reagent/latest-version.svg)

Add `data-frisk-reagent` to the dev `:dependencies` in your `project.clj`

## Usage

This library's public API consists of two public functions/reagent-components: `datafrisk.core/DataFriskShell` and `datafrisk.core/FriskInline`.


### DataFriskShell

This is what you see in the animation above. This component renders as a single data navigation "shell" fixed to the bottom of the window. It can be expanded/hidden via a toggle at the bottom right hand corner of the screen. 

Example:

```clojure
(ns datafrisk.demo
  (:require [reagent.core :as r]
            [datafrisk.core :as d]))

(defn mount-root []
  (r/render
    [d/DataFriskShell
     ;; List of arguments you want to visualize
     {:data {:some-string "a"
             :vector-with-map [1 2 3 3 {:a "a" :b "b"}]
             :a-set #{1 2 3}
             :a-map {:x "x" :y "y" :z [1 2 3 4]}
             :a-list '(1 2 3)
             :a-seq (seq [1 2])
             :an-object (clj->js {:a "a"})
             :this-is-a-very-long-keyword :g}}
     {:a :b :c :d}]
    (js/document.getElementById "app")))
```

### FriskInline

This component renders as a small box labeled "Data frisk" which expands into a navigator much like that found in the shell. It's perfect for situations where you want to frisk data from multiple components (or multiple instances of the same component, as with our `some-component` example above). Here's a quick demo.

```clojure
(ns datafrisk.demo
  (:require [reagent.core :as r]
            [datafrisk.core :as d]))

(defn some-component
  [name]
  [:div "Hi " name [d/FriskInline {:name name :testing 123}]])

(defn mount-root []
  (r/render
    [:div
     (for [x ["Bob" "Jo" "Ellen"]]
       [some-component x])]
    (js/document.getElementById "app")))
```

### Re-frame

See the [re-frisk](https://github.com/flexsurfer/re-frisk) project.

### For more

See the dev/demo.cljs namespace for example use.

## License

Copyright © 2017 Odin Standal

Distributed under the MIT License (MIT)
