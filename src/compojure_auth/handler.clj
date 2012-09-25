(ns compojure-auth.handler
  (:use [compojure.core]
        [compojure-auth.auth :as auth])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [compojure-auth.views.layout :as layout]))

;; Middleware

(defn logging-middleware
  "Generic logging middleware"
  [handler]
  (fn [req]
    (println req)
      (handler req)))

;; Application routes

(defroutes page-routes
  (GET "/" [] layout/home-page))

(defroutes login-routes
  (GET "/login"  [] (layout/login-form))
  (POST "/login" [] auth/login)
  (ANY "/logout" [] auth/logout))

(defroutes main-routes

  (auth/with-auth page-routes)
  
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes login-routes main-routes)
      (logging-middleware)
      (auth/with-user)
      (handler/site :session)))

(defn start-server []
  (future (jetty/run-jetty (var app) {:port 3000})))