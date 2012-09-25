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

(defn get-user
  [params]
  (auth/exists? (:user params) (:password params)))
  
(defn login [req]
  (let [user (get-user (:params req))
        home "/"
        url (if user home "/login")]
  (assoc-in (response/redirect url)
            [:session :user] user)))

(defn logout [req]
  (merge (response/redirect "/") {:session nil}))

(declare ^{:dynamic true} current-user)

(defn with-user-binding [handler] 
  (fn [request] 
    (binding [current-user (-> request :session :user)] 
      (handler request)))) 

(defn index-page [handler]
  (fn [request]
    (str "Hello " (:username current-user) "! <a href='/logout'>Logout</a>")))

;; Application routes

(defroutes page-routes
  (GET "/" [] index-page))

(defroutes main-routes
  
  (GET "/login"  [] (layout/login-form))
  (POST "/login" [] login)
  (ANY "/logout" [] logout)

  (auth/with-auth page-routes)
  
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> main-routes
      (logging-middleware)
      (with-user-binding)
      (handler/site :session)))

(defn start-server []
  (future (jetty/run-jetty (var app) {:port 3000})))