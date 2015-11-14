(ns visual.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [cljs.reader :as reader]
            [cljs.core.async :as async]
            [om-tools.core :refer-macros [defcomponent]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:import [goog.net XhrIo]))

(enable-console-print!)

(defonce app-state
  (atom {:text "Hello Chestnut!"
         :board #{}}))

(defn save-board [app board]
  (om/update! app [:board] (reader/read-string board)))

(defn get-board [c]
  (let [x (XhrIo.)]
    (.send goog.net.XhrIo "/board"
           (fn [e]
             (let [s (pr-str (-> e .-target .getResponse))
                   s' (reader/read-string s)]
               (async/put! c s'))) "GET")))

(defn alive-col []
  (rand-nth ["#efefef" "#ffffff"]))

(defn run-board-loop [c]
  (go-loop []
    (let [_ (async/<! (async/timeout 50))]
      (get-board c)
      (recur))))

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
                            (dom/td {:class (if (alive? board [y x]) "alive" "dead")
                                     :style {} #_(when (alive? board [y x]) {:background-color (alive-col)})} " "))))))))


(defcomponent board-c [app owner]
  (will-mount [_]
              (let [c (async/chan)]
                (run-board-loop c)
                (go-loop []
                  (let [b (async/<! c)]
                    (save-board app b)
                    (recur)))))
  (render [_]
          (dom/div {:class "board"}
                   (dom/h1 "Board")
                   (om/build board-table (:board app)))))

(defn main []
  (om/root
    board-c
    app-state
    {:target (. js/document (getElementById "app"))}))
