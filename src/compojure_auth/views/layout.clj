(ns compojure-auth.views.layout
  (:use [hiccup core page form]))

(defn main-layout [& content]
  (html5
    [:head
      [:title "Compojure Auth"]
      (include-css "/css/style.css")
      (include-css "/css/main.css")]
    [:body
     [:div {:class "container"}
       content]]))
  
(defn login-form  []
  (main-layout
    [:h1 "Username: "]
    [:div {:id "login"}
      (form-to [:post "/login"]        
        (label :user "Username")
        (text-field :user)
        (label :password "Password")
        (password-field :password)
        [:button {:type "submit"} "Log In"])
     ]))
 