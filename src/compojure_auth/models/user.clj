(ns compojure-auth.models.user
  (:use [compojure-auth.db :as db]))

(defn all []
  (db/q? ["SELECT * FROM users"]))

(defn exists? [username password]
  (let [results
        (db/q? ["SELECT id FROM users WHERE username = ? AND password = ?"
          username password])]
   (first results)))

;; Obviously the password needs to be hashed but just testing
(defn add [user]
  ^{:doc "Add a user to the database"}
  (let [{:keys [username password]} user]
    (db/insert! :users
      {:username username :password password})))
    
