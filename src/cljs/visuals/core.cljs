(ns visuals.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [visuals.events :as events]
   [visuals.routes :as routes]
   [visuals.views :as views]
   [visuals.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root)
  (re-frame/dispatch [::events/poll-server-loop]))
