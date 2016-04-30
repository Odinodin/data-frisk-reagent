(ns datadrill.ui-card
  (:require [devcards.core]
            [reagent.core :as r]
            [datadrill.core :refer [Root]])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg root
  Root
  (r/atom {:data-drill {:expansion #{[]}}
           :data "ape" :b "c"
           :c :d})
  {:inspect-data true})

