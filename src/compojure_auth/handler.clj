(ns compojure-auth.handler
  (:use [compojure.core]
        [ring.middleware.session.memory :only [memory-store]]
        [compojure-auth.models.user :only (exists?)]
        [ring.middleware.params :only [wrap-params]])
  (:require [compojure.route :as route]
            [noir.session :as session]
            [ring.adapter.jetty :as jetty]
            [compojure-auth.views.layout :as layout]
            [ring.util.response :as response]))

;; Login examples

;; Helper methods

(defn logging-middleware
  "Generic logging middleware"
  [app]
  (fn [req]
    (println
      (str "\nBegin "
      (get req :request-method)
      " "
      (get req :uri) "\n"))
    (println req)
    (app req)))

(defn logged-in? []
  (if (session/get :user_id)
    true
    false))

(defn login [params]
  (let [user (get params "user")
        pass (get params "password")]
    ;; check if the user exists, if so add user id to session
    ;; TODO encryption, flash messages
  (if (exists? user pass)
    (do (session/put! :user_id 1)
        (response/redirect "/"))
    (do
      (session/flash-put! :errors "Invalid user")
      (response/redirect "/login")))))

(defn index-page
  []
  (if (logged-in?)
    "You are logged in <a href='/logout'>Logout</a>"
    "Please log in <a href='/login'>Login</a>"))

(defn  logout []
   (do (session/remove! :user_id)
       (response/redirect "/")))

(defroutes resource-routes
  (-> (route/resources "/*")))

(defroutes auth-routes
  
  (GET "/" [] (index-page))
  
  (GET "/login" []
    (layout/login-form))
  
  (POST "/login" {params :params}
    (login params))
  
  (GET "/logout" []
    (logout))
  
  (route/resources "/*")
  (route/not-found "Not Found"))

(def app
  (-> (wrap-params auth-routes)
      logging-middleware
      (session/wrap-noir-session {:store (memory-store)})
      (session/wrap-noir-flash)))

(defn start-server []
  (future (jetty/run-jetty (var app) {:port 8080})))
