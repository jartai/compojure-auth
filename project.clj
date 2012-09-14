(defproject compojure-auth "0.1.0-SNAPSHOT"
  :description "Compojure sessions example"
  :url "http://owainlewis.com"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [lib-noir "0.1.1"]
                 [ring/ring-jetty-adapter "1.1.3"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [hiccup "1.0.1"]]
  :plugins [[lein-ring "0.7.3"]
            [lein-swank "1.4.4"]]
  :ring {:handler compojure-auth.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
