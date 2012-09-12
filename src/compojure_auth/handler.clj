(ns compojure-auth.handler
  (:use [compojure.core]
        [compojure-auth.views.layout :as layout]
        [ring.middleware.session :as session])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" {session :session} (layout/login session))
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      session/wrap-session))
