(ns controllers.authentication-controller
  (:use [conjure.core.controller.base])
  (:require [clojure.contrib.logging :as logging]
            [models.user :as user]
            [plugins.simple-authentication.login :as simple-login]))

(defn
#^{ :doc "Creates the authentication link list based on if the user is logged in or not." }
  link-list [request-map]
  (let [user-id (simple-login/logged-in? request-map)]
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

(defn
#^{ :doc "Adds the authentication links to the request-map if they are not already there." }
  update-request-map [request-map]
  (if (:links (:layout-info request-map))
    request-map
    (assoc request-map :layout-info { :links (link-list request-map) })))

(defn
#^{ :doc "Intercepts all authentication requests, and updates the request-map to display the correct actions." }
  request-map-interceptor [request-map action-fn]
  (let [new-request-map (update-request-map request-map)] 
    (action-fn new-request-map)))

(add-interceptor request-map-interceptor)

(defaction index
  (redirect-to request-map { :action "login" }))

(defaction login
  (bind request-map))

(defaction login-check
  (let [params (:params request-map)
        user (:user params)
        back-link (:back-link params)]
    (if (simple-login/login request-map (:name user) (:password user))
      (if back-link
        (redirect-to-full-url back-link)
        (redirect-to request-map { :controller "home", :action "index" }))
      (redirect-to request-map 
        { :action "login", 
          :params { :back-link back-link, :errors ["User name and password could not be found."] } }))))

(defaction logout
  (simple-login/logout request-map)
  (redirect-to request-map { :action "login" }))

(defaction create-user
  (bind request-map))

(defaction save-user
  (let [params (:params request-map)
        user (:user params)
        errors (user/full-verify-user user)]
    (if (and errors (not-empty errors))
      (redirect-to request-map { :action "create-user", :params { :user user, :errors errors } })
      (do
        (user/create user)
        (redirect-to request-map { :action "login" })))))

(defaction admin
  (bind request-map))

(defaction delete-verify
  (bind request-map))

(defaction delete-user
  (user/destroy-record { :id (:id (:params request-map)) })
  (redirect-to request-map { :action "admin" }))

(defaction edit-user
  (bind request-map))

(defaction edit-save
  (let [params (:params request-map)
        user (:user params)
        errors (user/full-verify-user user)]
    (if (and errors (not-empty errors))
      (redirect-to request-map { :action "edit-user", :params { :user user, :errors errors } })
      (do
        (user/update user)
        (redirect-to request-map { :action "admin" })))))

(defaction access-denied
  (bind request-map))