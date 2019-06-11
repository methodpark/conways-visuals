(ns visuals.handler
  (:require [compojure.core :refer [GET PUT defroutes] :as compojure]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :as response]
            [ring.util.request :as request]
            [ring.middleware.reload :refer [wrap-reload]]
            [visuals.world :as world]
            [cheshire.core :as json]))

(defn handle-store-world
  [req]
  (let [body      (-> req :body)
        new-world (-> body clojure.java.io/reader (json/parse-stream true))]
    (do
      (world/update-world! new-world)
      "ok")))

(defroutes api-routes
  (GET "/board" [] (response/response (world/get-world)))
  (PUT "/board" req (response/response (handle-store-world req))))

(defroutes routes
  (GET "/" [] (response/resource-response "index.html" {:root "public"}))
  (resources "/"))

(defn wrap-json-content
  [handler]
  (fn [req]
    (let [result (handler req)]
      (when (response/response? result)
        (update-in result [:body] #(json/generate-string %))))))

(defn wrap-json-content-type
  [handler]
  (fn [req]
    (-> req handler (response/content-type "application/json"))))

(def application-routes
  (compojure/routes
   (->
    api-routes
    wrap-json-content-type
    wrap-json-content)
   routes
   (not-found "This is not the content you are looking for")))

(def dev-handler (-> #'application-routes wrap-reload))

(def handler application-routes)


(comment

  (world/get-world)
  )
