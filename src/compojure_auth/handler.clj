(ns compojure-auth.handler
  (:use [compojure.core]
        [compojure-auth.models.user :as user]
        [compojure-auth.views.layout :as layout]
        [ring.middleware.session :as session])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn login-user
  [params]
  (let [username (get params "username")
        password (get params "password")]
  (if (user/exists? username password)
    "Found user"
    "No user found")))

(defroutes app-routes
  (GET "/" {session :session} (layout/login session))
  (POST "/login" {form-params :form-params} (login-user form-params))
                                                       
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      session/wrap-session))
