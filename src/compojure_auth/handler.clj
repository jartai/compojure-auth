(ns compojure-auth.handler
  (:use [compojure.core]
        [compojure-auth.models.user :as user]
        [compojure-auth.views.layout :as layout]
        [ring.middleware.session :as session])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn login-user
  [params session]
  (let [username (get params "username")
        password (get params "password")]
  (if (user/exists? username password)
    (do
      (assoc session :user_id 1)
      (str "Found user" session))
    "No user found")))

(defroutes app-routes
  (GET "/" {session :session} (layout/login session))
  (POST "/login" {session :session form-params :form-params} (login-user form-params session))
                                                       
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      session/wrap-session app-routes))
