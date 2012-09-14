(ns compojure-auth.handler
  (:use [compojure.core]
        [ring.middleware.session.memory :only [memory-store]]
        [compojure-auth.views.layout :as layout]
        [compojure-auth.models.user :only (exists?)]
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [noir.session :as session]
            [ring.util.response :as response]))

;; Login examples

;; Helper methods

(defn logged-in? []
  (if (session/get :user)
    true
    false))

(defn login [user password]
  ;; check if the user exists, if so add user id to session
  ;; TODO encryption, flash messages
  (if (exists? "owainlewis" "testing")
    (do (session/put! :user user)
        (response/redirect "/"))
    (session/flash-put! :errors "Invalid user")))

(defn index-page
  []
  (if (logged-in?)
    "You are logged in"
    "Please log in <a href='/login'>Login</a>"))

(defn  logout []
   (do (session/remove! :user)
       (response/redirect "/")))

(defroutes resource-routes
  (-> (route/resources "/*")))

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/login" []
    (layout/login-form))
  (POST "/login" {{:keys [user password]} :form-params}
    (login "owain" password))
  (GET "/logout" []
       (logout))
  (route/resources "/*")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (session/wrap-noir-session {:store (memory-store)})))

(defn start-server []
  (future (jetty/run-jetty (var app) {:port 8080})))
