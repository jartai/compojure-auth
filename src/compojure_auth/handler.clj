(ns compojure-auth.handler
  (:use [compojure.core]
        [compojure-auth.models.user]
        [compojure-auth.auth :as auth])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [compojure-auth.views.layout :as layout]
            [ring.util.response :as response]))

;; Session helpers

(defn session-set
  [k v])

(defn session-get
  [k])

(defn flash-set
  [req v]
  (assoc-in req [:flash :msg] v))

(defn flash-get
  [k])

(defn flash-test [handler]
  (fn [req]
     req))

;; Middleware

(defn logging-middleware
  "Generic logging middleware"
  [handler]
  (fn [req]
    (println req)
      (handler req)))

(defn get-user
  [{:keys [params]}]
  (exists? (:user params) (:password params)))
  
(defn login [req]
  (let [user (get-user req)
        url (if user "/" "/login")]
  (assoc-in (response/redirect url)
            [:session :user] user)))

(defn logout [req]
  (merge (response/redirect "/") {:session nil}))

(declare ^{:dynamic true} *current-user*)

(defn with-user-binding [handler] 
  (fn [request] 
    (binding [*current-user* (-> request :session :user)] 
      (handler request)))) 

(def index-page
  (str "You are logged in <a href='/logout'>Logout</a>"))

;; Application routes

(defroutes page-routes
  (GET "/" [] index-page)
  (GET "/flash" [] flash-test))

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