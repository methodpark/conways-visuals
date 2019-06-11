(ns visuals.events
  (:require
   [re-frame.core :as re-frame]
   [visuals.db :as db]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]))

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

(re-frame/reg-event-fx
 ::board-fetch-success
 (fn [cofx [_ result]]
   {:dispatch [::set-board result]}))


(re-frame/reg-event-fx
 ::poll-server-loop
 (fn [{:keys [db]} _]
   (let [ms    (-> db :poll-server-intervall)
         poll? (-> db :poll-server?)]
     {:dispatch-later [(when poll?
                         {:ms       0
                          :dispatch [::fetch-board]})
                       {:ms       ms
                        :dispatch [::poll-server-loop]}]})))

(re-frame/reg-event-db
 ::board-fetch-failure
 (fn [db [_ error]]
   (assoc db :error error)))

(re-frame/reg-event-fx
 ::toggle-poll
 (fn [{:keys [db]} _]
   (let [new-value (-> db :poll-server? not)]
     {:db       (assoc db :poll-server? new-value)
      :dispatch (when :new-value
                  [::fetch-board])})))

(re-frame/reg-event-db
 ::set-poll-intervall
 (fn [db [_ seconds]]
   (let [ms (* seconds 1000)]
     (assoc db :poll-server-intervall ms))))

(re-frame/reg-event-fx
 ::fetch-board
 (fn [cofx [_ _]]
   {:http-xhrio {:method          :get
                 :uri             "/board"
                 :response-format (ajax/json-response-format {:keywords? false})
                 :on-success      [::board-fetch-success]
                 :on-failure      [::board-fetch-failure]}}))

(re-frame/reg-event-db
 ::board-send-success
 (fn [db]
   (assoc db :message "Board was sent")))

(re-frame/reg-event-db
 ::board-send-failure
 (fn [db [_ error]]
   (assoc db :error error)))

(re-frame/reg-event-fx
 ::send-current-board
 (fn [{:keys [db]} [_ result]]
   {:http-xhrio {:method          :put
                 :uri             "/board"
                 :params          (->> db :board )
                 :response-format (ajax/json-response-format {:keywords? false})
                 :format          (ajax/json-request-format)
                 :on-success      [::board-send-success]
                 :on-failure      [::board-send-failure]}}))


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
