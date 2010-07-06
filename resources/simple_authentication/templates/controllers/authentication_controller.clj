(ns controllers.authentication-controller
  (:use [conjure.core.controller.base])
  (:require [clojure.contrib.logging :as logging]
            [conjure.core.server.request :as request]
            [models.user :as user]
            [plugins.simple-authentication.login :as simple-login]))

(defn
#^{ :doc "Creates the authentication link list based on if the user is logged in or not." }
  link-list []
  (let [user-id (simple-login/logged-in?)]
    (if user-id
      (let [logout-link { :text "Log Out", :url-for { :controller :authentication, :action :logout } }
            edit-profile-link 
              { :text "Edit Profile", 
                :url-for { :controller :authentication, :action :edit-user, :params { :id user-id } } }
            logged-in-links [logout-link edit-profile-link]]
        (if (simple-login/is-admin? (simple-login/current-user user-id))
          (cons { :text "Admin", :url-for { :controller :authentication, :action :admin } }
            logged-in-links) 
          logged-in-links))
      [{ :text "Log In", :url-for { :controller :authentication, :action :login } }
       { :text "Create Account", :url-for { :controller :authentication, :action :create-user } }])))

(defmacro
#^{ :doc "Adds the authentication links to the request-map if they are not already there." }
  update-request-map [& body]
  (if (:links (request/layout-info))
    `(do ~@body)
    `(request/with-merged-request-map { :layout-info { :links (link-list) } }
      ~@body)))

(defn
#^{ :doc "Intercepts all authentication requests, and updates the request-map to display the correct actions." }
  request-map-interceptor [action-fn]
  (update-request-map (action-fn)))

(add-interceptor request-map-interceptor)

(def-action index
  (redirect-to { :action "login" }))

(def-action login
  (bind))

(def-action login-check
  (let [params (request/parameters)
        user (:user params)
        back-link (:back-link params)]
    (if (simple-login/login (:name user) (:password user))
      (if back-link
        (redirect-to-full-url back-link)
        (redirect-to { :controller "home", :action "index" }))
      (redirect-to
        { :action "login", 
          :params { :back-link back-link, :errors ["User name and password could not be found."] } }))))

(def-action logout
  (simple-login/logout)
  (redirect-to { :action "login" }))

(def-action create-user
  (bind))

(def-action save-user
  (let [user (:user (request/parameters))
        errors (user/full-verify-user user)]
    (if (and errors (not-empty errors))
      (redirect-to { :action "create-user", :params { :user user, :errors errors } })
      (do
        (user/create user)
        (redirect-to { :action "login" })))))

(def-action admin
  (bind))

(def-action delete-verify
  (bind))

(def-action delete-user
  (user/destroy-record { :id (request/id) })
  (redirect-to { :action "admin" }))

(def-action edit-user
  (bind))

(def-action edit-save
  (let [user (:user (request/parameters))
        errors (user/full-verify-user user)]
    (if (and errors (not-empty errors))
      (redirect-to { :action "edit-user", :params { :user user, :errors errors } })
      (do
        (user/update user)
        (redirect-to { :action "admin" })))))

(def-action access-denied
  (bind))