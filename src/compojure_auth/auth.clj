(ns compojure-auth.auth
  (:refer-clojure :exclude [compare])
  (:use [compojure-auth.db :as db])
  (:require [noir.util.crypt :as crypt]
            [ring.util.response :as response]))

(defn logged-in? [req]
  (get-in req [:session :user] false))

(defn with-auth [handler]
  "Authentication handler that redirects if a user is not logged in"
  (fn [req]
    (let [login-path "/login"
          uri (:uri req)]
      (if (or (logged-in? req)
              (.startsWith uri "/css")
              (.startsWith uri "/javascripts"))
        (handler req)
        (response/redirect login-path)))))

(defn exists? [username password]
  "If a user exists return selected fields else return false"
  (let [pw (crypt/encrypt password)
        results
        (db/q? ["SELECT id, username FROM users WHERE username = ?" username])]
   (if (crypt/compare password pw)
     (first results)
     false)))
