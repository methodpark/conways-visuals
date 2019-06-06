(ns visuals.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [visuals.core-test]))

(doo-tests 'visuals.core-test)
