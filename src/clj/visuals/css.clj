(ns visuals.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:.dead {:background-color "white"}]
  [:.alive {:background-color "green"}])
