(ns controllers.authentication-controller
  (:use [conjure.controller.base])
  (:require [models.user :as user]))

(defn
#^{ :doc "Intercepts all authentication requests, and updates the request-map to display the correct actions." }
  request-map-interceptor [request-map action-fn]
  (action-fn
    (assoc 
      request-map 
      :layout-info 
      { :links 
        [{ :text "Login", :url-for { :controller :authentication, :action :login } }
         { :text "Create Account", :url-for { :controller :authentication, :action :create-user } }]})))

(add-interceptor request-map-interceptor)

(defaction index
  (redirect-to request-map { :action "login" }))

(defaction login
  (bind request-map))

(defaction logout
  (bind request-map))

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

(defaction delete-user
  (bind request-map))

(defaction edit-user
  (bind request-map))