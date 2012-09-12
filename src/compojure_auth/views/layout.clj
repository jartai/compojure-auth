(ns compojure-auth.views.layout
  (:use [hiccup.core]))

(defn main-layout [& body])

(defn login [session]
  (html
  [:form {:method "POST" :action "/login"}
    [:label "Username"]
    [:input {:type "text"}]
    [:label "Password"]
    [:input {:type "text"}]
  ]))
   