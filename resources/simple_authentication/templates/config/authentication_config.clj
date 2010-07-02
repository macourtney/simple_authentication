(ns authentication-config
  (:require [plugins.simple-authentication.authentication-interceptors :as auth-interceptors]))

(defn
#^{ :doc "Sets up authentication. Called by the simple authentication plugin at initialization time." }
  configure []
  (auth-interceptors/init-login-interceptor 
    { :home :all, 
      :authentication #{ :login, :login-check, :create-user, :save-user, :access-denied } })
  (auth-interceptors/init-admin-interceptor { :authentication #{ :admin } })
  (auth-interceptors/init-admin-or-user-interceptor { :authentication #{ :edit-user } }))