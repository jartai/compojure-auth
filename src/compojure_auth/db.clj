(ns compojure-auth.db
  (:require [clojure.java.jdbc :as sql]))

(defn db [db-name & opts]
  (let [[user password] opts]
    {:subprotocol "mysql"
     :subname "//127.0.0.1:3306/compojure"
     :user "root"
     :password ""}))

(defn create-database []
  (sql/create-table
    :users
    [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
    [:username "varchar(255)"]
    [:password "varchar(255)"]))

(defmacro with-conn [& body]
  `(sql/with-connection (db "compojure")
     (do ~@body)))

(defn create []
  (with-conn
    (create-database)))

(defmacro q? [query & fn]
  ^{:doc "generic query abstraction. Returns map of results"}
  `(let [retval#
     (with-conn
      (sql/with-query-results r#
        ~query
        (into [] r#)))]
     retval#))

(defn insert! [table values]
  ^{:doc "insert a record"}
  (with-conn
    (sql/insert-records table
      values)))