(ns datafrisk.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            datafrisk.view-test))

(doo-tests
  'datafrisk.view-test)
