(ns datadrill.ui-card
  (:require [devcards.core]
            [reagent.core :as r]
            [datadrill.core :refer [Root]])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg root
  [Root (r/atom {:a "a"
                 :b [1 2 3]
                 :c :d})])

(defcard-rg first-level-expanded
  [Root (r/atom {:data-drill {:expansion #{[]}}
                 :a "a"
                 :b [1 2 3]
                 :c :d})])

(defcard-rg second-level-expanded
  [Root (r/atom {:data-drill {:expansion #{[] [:b]}}
                 :a "a"
                 :b [1 2 3]
                 :c :d})])