(ns compojure-auth.auth
  (:refer-clojure :exclude [compare])
  (:use [compojure-auth.db :as db])
  (:require [noir.util.crypt :as crypt]
            [ring.util.response :as response]))

(declare ^{:dynamic true} current-user)

(defn logged-in? [req]
  "Returns true if a user is logged in or false"
  (get-in req [:session :user] false))

(defn with-user [handler] 
  (fn [request] 
    (binding [current-user (-> request :session :user)] 
      (handler request))))

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

(defn get-user [params] (exists? (:user params) (:password params)))

;; Main login/logout actions

(defn login [req]
  (let [user (get-user (:params req))
        home "/"
        url (if user home "/login")]
  (assoc-in (response/redirect url)
            [:session :user] user)))

(defn logout [req]
  (merge (response/redirect "/") {:session nil}))