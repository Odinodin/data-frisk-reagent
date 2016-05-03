(ns datafrisk.ui-card
  (:require [devcards.core]
            [reagent.core :as r]
            [datafrisk.core :refer [Root]])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg data-types
  [Root (r/atom {:a "I'm a string"
                 :b :imakeyword
                 :c [1 2 3]
                 :d '(1 2 3)
                 :e #{1 2 3}
                 :f (clj->js {:i-am "an-object"})})])

(defcard-rg first-level-expanded
  [Root (r/atom {:data-frisk {:expanded-paths #{[]}}
                 :a "a"
                 :b [1 2 3]
                 :c :d})])

(defcard-rg second-level-expanded
  [Root (r/atom {:data-frisk {:expanded-paths #{[] [:b]}}
                 :a "a"
                 :b [1 2 3]
                 :c :d})])