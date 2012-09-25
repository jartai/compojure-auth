(ns compojure-auth.models.user
  (:refer-clojure :exclude [compare])
  (:use [compojure-auth.db :as db]
        [noir.util.crypt :as crypt]))

(defn all []
  (db/q? ["SELECT * FROM users"]))

(defn exists? [username password]
  "If a user exists return selected fields else return false"
  (let [pw (crypt/encrypt password)
        results
        (db/q? ["SELECT id, username FROM users WHERE username = ?" username])]
   (if (crypt/compare password pw)
     (first results)
     false)))

(defn add [user]
  ^{:doc "Add a user to the database"}
  (let [{:keys [username password]} user]
    (db/insert! :users
      {:username username :password (crypt/encrypt password)})))
    
