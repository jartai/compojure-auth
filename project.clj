(defproject compojure-auth "0.1.0-SNAPSHOT"
  :description "Compojure sessions example"
  :url "http://owainlewis.com"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [hiccup "1.0.1"]]
  :plugins [[lein-ring "0.7.3"]]
  :ring {:handler compojure-auth.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
