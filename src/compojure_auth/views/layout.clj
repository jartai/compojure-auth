(ns compojure-auth.views.layout
  (:use [hiccup core page]))

(defn main-layout [& content]
  (html5
    [:head
      [:title "Compojure Auth"]
      (include-css "/css/style.css")
      (include-css "/css/main.css")]
    [:body
     [:div {:class "container"}
       content]]))
  
(defn login [session]
  (main-layout
    [:div {:id "login"}
    [:form {:method "POST" :action "/login"}
      [:label "Username"]
      [:input {:type "text" :name "username"}]
      [:label "Password"]
      [:input {:type "text" :name "password"}]
      [:input {:type "submit" :class "btn" :value "Login"}]
    ]]))
   