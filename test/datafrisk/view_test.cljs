(ns datafrisk.view-test
  (:require [cljs.test :refer-macros [are deftest is]]
            [datafrisk.view :as sut]
            [reagent.core :as r]))

(deftest first-test
  (is (= (sut/expand-all-paths
           {:a 1} {})
         {[] {:expanded? true}}))

  (is (= (sut/expand-all-paths
           {:a {:b 1}} {})
         {[] {:expanded? true}
          [:a] {:expanded? true}}))

  (is (= (sut/expand-all-paths
           {:a {:c 1} :b {:d 2}} {})
         {[] {:expanded? true}
          [:a] {:expanded? true}
          [:b] {:expanded? true}}))

  (is (= (sut/expand-all-paths
           {:a [1 2 3]} {})
         {[] {:expanded? true}
          [:a] {:expanded? true}}))

  (is (= (sut/expand-all-paths
           {:a [1 {:b [2 3 4]}]} {})
         {[] {:expanded? true}
          [:a] {:expanded? true}
          [:a 1] {:expanded? true}
          [:a 1 :b] {:expanded? true}}))

  (is (= (sut/expand-all-paths
           (r/atom {:a 1}) {})
         {[] {:expanded? true}}))

  (is (= (sut/expand-all-paths
           (r/atom {:a {:b 1}}) {})
         {[] {:expanded? true}
          [:a] {:expanded? true}})))