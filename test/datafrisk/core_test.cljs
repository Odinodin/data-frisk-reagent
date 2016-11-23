(ns datafrisk.core-test
  (:require [cljs.test :refer-macros [are deftest is]]
            [datafrisk.core :as sut]
            [reagent.core :as r]))

(deftest first-test
  (is (= (sut/expand-all-paths
           {:a 1})
         #{[]}))

  (is (= (sut/expand-all-paths
           {:a {:b 1}})
         #{[] [:a]}))

  (is (= (sut/expand-all-paths
           {:a {:c 1} :b {:d 2}})
         #{[] [:a] [:b]}))

  (is (= (sut/expand-all-paths
           {:a [1 2 3]})
         #{[] [:a]}))

  (is (= (sut/expand-all-paths
           {:a [1 {:b [2 3 4]}]})
         #{[] [:a] [:a 1] [:a 1 :b]}))

  (is (= (sut/expand-all-paths
           (r/atom {:a 1}))
         #{[]}))

  (is (= (sut/expand-all-paths
           (r/atom {:a {:b 1}}))
         #{[] [:a]})))