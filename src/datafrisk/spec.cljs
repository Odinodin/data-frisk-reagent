(ns datafrisk.spec
  (:require [datafrisk.view :refer [Root]]
            [reagent.core :as r]))

(defn spec-problem->metadata-path [{:keys [in path pred val via]}]
  [in {:error (str "(not "
                     (clojure.string/replace (str pred) "cljs.core/" "")
                     ")")}])

(defn frisk-errors [id errors]
  {:data (:cljs.spec.alpha/value errors)
   :state (r/atom {:data-frisk {id {:metadata-paths (-> (into {} (map spec-problem->metadata-path (:cljs.spec.alpha/problems errors)))
                                                        (update [] assoc :expanded? true))}}})})

(defn SpecView [{:keys [errors]}]
  (let [mangled (frisk-errors "spec-errors" errors)]
    [Root (:data mangled) "spec-errors" (:state mangled)]))

(defn SpecTitleView [{:keys [errors title] :as args}]
  [:div {:style {:background-color "white"
                 :padding "10px"}}
   (if title
     [:div {:style (:style title {})} (:text title)]
     [:div {}
      [:span {:style {:font-weight "700" :color "red"}} "Did not comply with spec "]
      [:span {:style {}} (str (:cljs.spec.alpha/spec errors))]])
   [SpecView args]])