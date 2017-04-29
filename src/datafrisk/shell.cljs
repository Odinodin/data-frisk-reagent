(ns datafrisk.shell
  (:require [datafrisk.view :as view]
            [reagent.core :as r]))

(def styles
  {:shell {:backgroundColor "#FAFAFA"
           :fontFamily "Consolas,Monaco,Courier New,monospace"
           :fontSize "12px"
           :z-index 9999}
   :shell-visible-button {:backgroundColor "#4EE24E"}})

(defn DataFriskShellVisibleButton [visible? toggle-visible-fn]
  [:button {:onClick toggle-visible-fn
            :style (merge {:border 0
                           :cursor "pointer"
                           :font "inherit"
                           :padding "12px"
                           :position "fixed"
                           :right 0
                           :width "80px"
                           :text-align "center"}
                          (:shell-visible-button styles)
                          (when-not visible? {:bottom 0}))}
   (if visible? "Hide" "Data frisk")])

(defn DataFriskShellView [shell-state & data]
  (let [visible? (:shell-visible? @shell-state)]
    [:div {:style (merge {:position "fixed"
                          :right 0
                          :bottom 0
                          :width "100%"
                          :height "50%"
                          :max-height (if visible? "50%" 0)
                          :transition "all 0.3s ease-out"
                          :padding 0}
                         (:shell styles))}
     [DataFriskShellVisibleButton visible? (fn [_] (swap! shell-state assoc :shell-visible? (not visible?)))]
     [:div {:style {:padding "10px"
                    :height "100%"
                    :box-sizing "border-box"
                    :overflow-y "scroll"}}
      (map-indexed (fn [id x]
                     ^{:key id} [view/Root x id shell-state]) data)]]))

(defn DataFriskShell [& data]
  (let [expand-by-default (reduce #(assoc-in %1 [:data-frisk %2 :expanded-paths] #{[]}) {} (range (count data)))
        shell-state (r/atom expand-by-default)]
    (fn [& data]
      (apply DataFriskShellView shell-state data))))

