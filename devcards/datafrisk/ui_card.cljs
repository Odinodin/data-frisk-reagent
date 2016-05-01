(ns datafrisk.ui-card
  (:require [devcards.core]
            [reagent.core :as r]
            [datafrisk.core :refer [Root]])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg root
  [Root (r/atom {:a "a"
                 :b [1 2 3]
                 :c :d})])

(defcard-rg first-level-expanded
  [Root (r/atom {:data-frisk {:expansion #{[]}}
                 :a "a"
                 :b [1 2 3]
                 :c :d})])

(defcard-rg second-level-expanded
  [Root (r/atom {:data-frisk {:expansion #{[] [:b]}}
                 :a "a"
                 :b [1 2 3]
                 :c :d})])