(ns datafrisk.spec-card
  (:require [devcards.core]
            [cljs.spec.alpha :as s]
            [reagent.core :as r]
            [datafrisk.spec :refer [SpecView SpecTitleView]])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(s/def :person/name string?)
(s/def :person/age number?)
(s/def :person/address string?)

(s/def :person/person (s/keys :req [:person/name
                                    :person/age
                                    :person/address]))

(s/def :app/persons (s/coll-of :person/person))

(defcard-rg spec-view
  [SpecView
   {:errors (s/explain-data :person/person {:likes 2
                                            :person/name 1
                                            :person/age "Jane"})}])

(defcard-rg spec-title-view
  [SpecTitleView
   {:errors (s/explain-data :person/person {:likes 2
                                            :person/name 1
                                            :person/age "Jane"})}])

(defcard-rg bad-vec
  [SpecTitleView
   {:errors (s/explain-data :app/persons [1 2 3 [4 5]])}])

(defcard-rg bad-list
  [SpecTitleView
   {:errors (s/explain-data :app/persons '(1 2 3 (4 5 )))}])

(defcard-rg bad-set
  [SpecTitleView
   {:errors (s/explain-data :app/persons #{1 2 #{3 4} 5})}])

(defcard-rg bad-nested-map
  [SpecTitleView
   {:errors (s/explain-data :app/persons [{:likes 2
                                           :person/name 1
                                           :person/age "Jane"}
                                          {:likes 3
                                           :person/name 2
                                           :person/age "Jenna"}])}])

(defcard-rg bad-string
  [SpecTitleView
   {:errors (s/explain-data :app/persons "some string")}])

(defcard-rg override-spec-title
  [SpecTitleView
   {:title {:style {:font-weight "700" :color "red"}
            :text "What ever you want"}
    :errors (s/explain-data :app/persons "some string")}])