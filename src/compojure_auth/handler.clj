(ns compojure-auth.handler
  (:use [compojure.core]
        [ring.middleware.session.cookie :only [cookie-store]]
        [compojure-auth.views.layout :as layout]
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [noir.session :as session]
            [ring.util.response :as response]))

(defn login [user password]
    (session/put! :user user)
    (response/redirect "/session"))

(defn  logout [user]
  '())

(defroutes app-routes
  
  (GET "/" []
    (layout/login-form))

  (GET "/session" []
    (let [session (session/get :user)]
      (str "session is: " session)))
  
  (POST "/login" {{:keys [user password]} :form-params}
    (login "owain" password))
  
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (session/wrap-noir-session {:store (cookie-store)})))

(defn start-server []
  (future (jetty/run-jetty (var app) {:port 8080})))
