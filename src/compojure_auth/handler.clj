(ns compojure-auth.handler
  (:use [compojure.core]
        [compojure-auth.views.layout :as layout]
        [ring.middleware.session :only (wrap-session)]
        [ring.util.response :only (response)])
  (:require [ring.adapter.jetty :as jetty]
            [compojure.route :as route]))

(defn counter-handler [req]
  (let [cnt (inc (-> req :session (:counter 0)))
        res (response (format "You visited %d times:\n\n%s"
                       cnt (with-out-str (println req))))]
    (assoc-in res [:session :counter] cnt)))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn log-request [app]
  (fn [req]
    (println req)
    (app req)))

(def app
  (-> counter-handler
      wrap-session))

(defn start-server []
  (future (jetty/run-jetty (var app) {:port 8080})))
