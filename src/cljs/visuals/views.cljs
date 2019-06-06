(ns visuals.views
  (:require
   [re-frame.core :as re-frame]
   [visuals.subs :as subs]
   [visuals.events :as events]))


(defn Cell
  [alive?]
  [:td.cell {:class (if alive? "alive" "dead")}])

(defn Board
  []
  (let [board    (re-frame/subscribe [::subs/board])
        num-cols (re-frame/subscribe [::subs/num-cols])
        num-rows (re-frame/subscribe [::subs/num-rows])]
    [:div.board
     [:h3 "Board"]
     [:table>tbody
      (doall
       (for [j (range @num-rows)]
         ^{:key (str "row" j)}
         [:tr
          (doall
           (for [i (range @num-cols)]
             (let [coords [i j]
                   alive? (contains? @board coords)]
               ^{:key (str i j)} [Cell alive?])))]))]]))

;; home

(defn Debug
  ""
  []
  [:div.debug
   [:h2 "debug"]
   [:button {:on-click (fn [] (re-frame/dispatch [::events/randomize-board]))} "Randomize world"]])

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [Debug]
     [Board]]))


;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:href "#/"}
     "go to Home Page"]]])


;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
