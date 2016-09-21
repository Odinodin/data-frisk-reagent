# data-frisk-reagent

> "Get your facts first, then you can distort them as you please" - Mark Twain

Visualize your data in your Reagent apps as a tree structure.

Suitable for use during development.

<img src="data-frisk.gif">

## Install

Add `[data-frisk-reagent "0.2.6"]` to the dev `:dependencies` in your `project.clj`

## Usage

This library's public API consists of two public functions/reagent-components: `datafrisk.core/DataFriskShell` and `datafrisk.core/FriskInline`.


### DataFriskShell

This is what you see in the gif animation above. This component renders as a single data navigation "shell" fixed to the bottom of the window. It can be expanded/hidden via a toggle at the bottom right hand corner of the screen. It's best when all the data you want to frisk is within scope within a single parent component.

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

However, if you did something like:

```clojure
(ns datafrisk.demo
  (:require [reagent.core :as r]
            [datafrisk.core :as d]))

(defn some-component
  [name]
  [:div "Hi " name [d/DataFriskShell {:name name :testing 123}]])

(defn mount-root []
  (r/render
    [:div
     (for [x ["Bob" "Jo" "Ellen"]]
       [some-component x])]
    (js/document.getElementById "app")))
```

Then you'll find that one of the `:testing` maps will end up being accessible via the shell.
For this situation, we have the following:


### FriskInline

This component renders as a small box label "data frisk" which expands into a navigator much like that found in the shell. It's perfect for situations where you want to frisk data from multiple components (or multiple instances of the same component, as with our `some-component` example above). Here's a quick demo.


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

Et viola!

### Use with re-frame

This example uses [re-frame](https://github.com/Day8/re-frame) to display a data-frisk component displaying the current app database:

```clojure
(ns datafrisk.re-frame-example
  (:require [reagent.core :as r]
            [datafrisk.core :as d]
            [re-frame.core :refer [subscribe reg-sub]]))

;; Set up a subscription
(defn- app-db-subscription
  "Subscribe to any change in the app db under the path"
  [db [_ path]]
  (get-in db path))
(reg-sub :debug/everything app-db-subscription)

;; Define a form-2 component
(defn frisk
  [& path]
  (let [everything (subscribe [:debug/everything path])]
    (fn [& path]
      [d/DataFriskShell @everything])))

;; Now you can use the component thusly:

(defn mount-root []
  (r/render
    [:div
     [:h1 "Welcome to ZomboCom"]

     ;; This displays the entire app database:
     [frisk]

     ;; This displays everything under a specific subtree:
     [frisk :subtree :that :interests-me]]

    (js/document.getElementById "app")))
```

### For more

See the dev/demo.cljs namespace for more usage of the shell.


## License

Copyright © 2016 Odin Standal

Distributed under the MIT License (MIT)
