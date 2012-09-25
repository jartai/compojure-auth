(ns compojure-auth.handler
  (:use [compojure.core]
        [compojure-auth.auth :as auth])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [compojure-auth.views.layout :as layout]
            [ring.util.response :as response]))

;; Middleware

(defn logging-middleware
  "Generic logging middleware"
  [handler]
  (fn [req]
    (println req)
      (handler req)))

(defn login [req]
  (let [user (auth/get-user (:params req))
        home "/"
        url (if user home "/login")]
  (assoc-in (response/redirect url)
            [:session :user] user)))

(defn logout [req]
  (merge (response/redirect "/") {:session nil}))

;; Application routes

(defroutes page-routes
  (GET "/" [] layout/home-page))

(defroutes login-routes
  (GET "/login"  [] (layout/login-form))
  (POST "/login" [] login)
  (ANY "/logout" [] logout))

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