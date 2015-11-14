(ns visual.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [visual.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'visual.core-test))
    0
    1))
