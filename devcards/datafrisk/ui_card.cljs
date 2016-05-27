(ns datafrisk.ui-card
  (:require [devcards.core]
            [reagent.core :as r]
            [datafrisk.core :refer [Root]])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg data-types
  [Root
   {:a "I'm a string"
    :b :imakeyword
    :c [1 2 3]
    :d '(1 2 3)
    :e #{1 2 3}
    :f (clj->js {:i-am "an-object"})}
   "root"
   (r/atom {})])

(defcard-rg first-level-expanded
  [Root
   {:a "a"
    :b [1 2 3]
    :c :d}
   "root"
   (r/atom {:data-frisk {"root" {:expanded-paths #{[]}}}})])

(defcard-rg second-level-expanded
  [Root {:a "a"
         :b [1 2 3]
         :c :d}
   "root"
   (r/atom {:data-frisk {"root" {:expanded-paths #{[] [:b]}}}})])

(defcard-rg empty-collections
  [Root {:set #{}
         :vec []
         :list '()}
   "root"
   (r/atom {:data-frisk {"root" {:expanded-paths #{[]}}}})])