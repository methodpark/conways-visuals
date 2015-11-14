(ns visual.server
  (:require [clojure.java.io :as io]
            [visual.dev :refer [is-dev? inject-devmode-html browser-repl start-figwheel]]
            [compojure.core :refer [GET PUT defroutes]]
            [compojure.route :refer [resources]]
            [net.cgrand.enlive-html :refer [deftemplate]]
            [cheshire.core :as json]
            [net.cgrand.reload :refer [auto-reload]]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.browser-caching :refer [wrap-browser-caching]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(deftemplate page (io/resource "index.html") []
  [:body] (if is-dev? inject-devmode-html identity))

(def state (atom #{[1 2] [1 3] [0 0] [2 3] [5 5]}))

(defn save-board [r]
  (try
    (let [pl (:body r)
          j (json/parse-string (slurp pl) true)]
      (println pl)
      (reset! state (set (:living j)))
      "okay")
    (catch Exception e
      "nope")))

(defroutes routes
  (resources "/")
  (resources "/react" {:root "react"})
  (PUT "/board" req  (save-board req))
  (GET "/board" req (pr-str @state))
  (GET "/*" req (page)))

(defn- wrap-browser-caching-opts [handler] (wrap-browser-caching handler (or (env :browser-caching) {})))

(def http-handler
  (cond-> routes
    true (wrap-defaults api-defaults)
    is-dev? reload/wrap-reload
    true wrap-browser-caching-opts
    true wrap-gzip))

(defn run-web-server [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (println (format "Starting web server on port %d." port))
    (run-jetty http-handler {:port port :join? false})))

(defn run-auto-reload [& [port]]
  (auto-reload *ns*)
  (start-figwheel))

(defn run [& [port]]
  (when is-dev?
    (run-auto-reload))
  (run-web-server port))

(defn -main [& [port]]
  (run port))

