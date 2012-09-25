(ns compojure-auth.models.user
  (:use [compojure-auth.db :as db]
        [noir.util.crypt :as crypt]))

(defn all []
  (db/q? ["SELECT * FROM users"]))

(defn add [user]
  ^{:doc "Add a user to the database"}
  (let [{:keys [username password]} user]
    (db/insert! :users
      {:username username :password (crypt/encrypt password)})))