(ns compojure-auth.handler
  (:use [compojure.core]
        [ring.middleware.session.memory :only [memory-store]]
        [compojure-auth.views.layout :as layout]
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [noir.session :as session]
            [ring.util.response :as response]))

;; Login examples

(defn find-authorized-user
  "Mocking db call"
  [user password]
  (boolean (and (= user "owain")
                (= password "password"))))

(defn login [user password]
  ;; check if the user exists, if so add user id to session
  (if (= (.toLowerCase user) "owain")
    (do (session/put! :user user)
        (response/redirect "/session"))
    (session/flash-put! :errors "Invalid user")))

(defn  logout []
   (do (session/remove! :user)
       (response/redirect "/")))

(defroutes app-routes
  
  (GET "/" []
    (layout/login-form))

  (GET "/session" []
       (let [session (session/get :user)]
      (str "session is: " session)))
  
  (POST "/login" {{:keys [user password]} :form-params}
    (login "owain" password))

  (GET "/logout" []
    (logout))
  
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (session/wrap-noir-session {:store (memory-store)})))

(defn start-server []
  (future (jetty/run-jetty (var app) {:port 8080})))
