(ns datafrisk.core
  (:require [datafrisk.view :as view]
            [datafrisk.shell :as shell]))

(defn DataFriskShell [& data]
  (apply shell/DataFriskShell data))

(defn DataFriskView [& data]
  (apply view/DataFriskView data))

;; Deprecated
(defn FriskInline [& data]
  (apply view/DataFriskView data))


