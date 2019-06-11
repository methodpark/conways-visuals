(ns visuals.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::board
 (fn [db]
   (:board db)))

(re-frame/reg-sub
 ::num-cols
 :<- [::board]
 (fn [board]
   (->> board (map first) (apply max) inc)))

(re-frame/reg-sub
 ::error
 (fn [db]
   (:error db)))

(re-frame/reg-sub
 ::poll-server?
 (fn [db]
   (:poll-server? db)))

(re-frame/reg-sub
 ::poll-server-intervall
 (fn [db]
   (:poll-server-intervall db)))

(re-frame/reg-sub
 ::poll-server-intervall-in-seconds
 :<- [::poll-server-intervall]
 (fn [interval]
   (-> interval (/ 1000) int)))

(re-frame/reg-sub
 ::num-rows
 :<- [::board]
 (fn [board]
   (->> board (map second) (apply max) inc)))


(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))
