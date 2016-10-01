(ns datafrisk.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            datafrisk.core-test))

(doo-tests
  'datafrisk.core-test)
