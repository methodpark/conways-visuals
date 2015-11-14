(ns visual.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(def board #{[0 0] [1 1] [2 4]})

(enable-console-print!)

(def app-state
  (atom {:text "Hello Chestnut!"
         :board board}))

(defn max-col [board]
  (->> board (map first) (apply max)))

(defn max-row [board]
  (->> board (map second) (apply max)))

(defn alive? [board cell]
  (board cell))

(defcomponent board-table [board owner]
  (render [_]
          (let [cols (max-col board)
                rows (max-row board)]
            (dom/table (for [x (range 0 (inc rows))]
                         (dom/tr
                          (for [y (range 0 (inc cols))]
                            (dom/td {:class (if (alive? board [y x]) "alive" "dead")} " "))))))))


(defcomponent board-c [app owner]
  (render [_]
          (dom/div {:class "board"}
                   (dom/h1 "Board")
                   (om/build board-table (:board app)))))

(defn main []
  (om/root
    board-c
    app-state
    {:target (. js/document (getElementById "app"))}))
