(ns datafrisk.spec
  (:require [datafrisk.view :refer [Root]]
            [reagent.core :as r]))

(defn spec-problem->metadata-path [{:keys [in path pred val via]}]
  [in {:error (str "\u00A0 (not "
                     (clojure.string/replace (str pred) "cljs.core/" "")
                     ")")}])

(defn frisk-errors [id errors]
  {:data (:cljs.spec.alpha/value errors)
   :state (r/atom {:data-frisk {id {:metadata-paths (->> (map spec-problem->metadata-path (:cljs.spec.alpha/problems errors))
                                                         (into {}))}}})})

(defn SpecView [{:keys [errors]}]
  (let [mangled (frisk-errors "spec-errors" errors)]
    [:div
     [:div {:style {:background-color "#d9534f"
                    :border-radius "6px 6px 0 0"
                    :padding "10px"
                    :color "#99000C"}} (str "Error by spec " (:cljs.spec.alpha/spec errors))]
     [:div {:style {:background-color "white"
                    :border "1px solid #d9534f"
                    :border-radius "0 0 6px 6px"
                    :padding "10px"}}
      [Root (:data mangled) "spec-errors" (:state mangled)]]]))