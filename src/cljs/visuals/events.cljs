(ns visuals.events
  (:require
   [re-frame.core :as re-frame]
   [visuals.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-board
 (fn [db [_ board]]
   (assoc db :board (set board))))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(defn rand-board [& {:keys [max-living max-x max-y]
                     :or   {max-living 5000
                            max-x      150
                            max-y      150}}]
  (let [gen-fun #(vector (rand-int max-x) (rand-int max-y))]
    (-> (repeatedly max-living gen-fun) set)))

(re-frame/reg-event-fx
 ::randomize-board
 (fn [cofx [_ _]]
   (let [random-board (rand-board)]
     {:dispatch [::set-board random-board]})))
