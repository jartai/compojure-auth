(ns compojure-auth.views.layout
  (:use [hiccup core page form]))

;; View helper functions

(defn flash-messages
  "Show flash messages if they exist in a request"
  [req]
  (declare flash-message)
  (if-let [flash-message (:flash req)]
    [:div {:id "flash"}
      flash-message]))

(defn main-layout [& content]
  (html5
    [:head
      [:title "Compojure Auth"]
      (include-css "css/style.css")
      (include-css "css/main.css")]
    [:body
     [:div {:class "container"}
      content]]))

(defn home-page [current-user]
  (main-layout
    [:h1 (str "Hello " (:username current-user))]
      [:p
        [:a {:href "/logout"} "Logout"]]))
  
(defn login-form  [req]
  (main-layout
   [:div {:id "login"}
     [:h2 "Login"]
     (flash-messages req)
      (form-to [:post "/login"]        
        (label :user "Username")
        (text-field :user)
        (label :password "Password")
        (password-field :password)
        [:button {:class "btn-blue" :type "submit"} "Log In"])
     ])) 