(ns plugins.simple-authentication.login
  (:import [clojure.lang PersistentArrayMap]) 
  (:require [clojure.contrib.logging :as logging]
            [conjure.core.model.session-store :as session-store]
            [models.user :as user]
            [config.session-config :as session-config]))

(def session-key :user-id) 

(defn
#^{ :doc "Logs the given user into the system and returns the user. If the user does not exist or the password is
incorrect this function returns nil." }
  login [user-name user-password]
  (let [user (user/find-user user-name user-password)]
    (when user
      (session-store/save session-key (:id user))
      user)))

(defn
#^{ :doc "If the requestor is logged in, then this function returns the user id. Otherwise, this function returns 
nil" }
  logged-in? [] 
  (session-key (session-store/retrieve)))

(defmulti current-user type)

(defmethod current-user String [user-id]
  (when user-id
    (user/find-record { :id user-id }))) 

(defmethod current-user Integer [user-id]
  (when user-id
    (user/find-record { :id user-id })))

(defmethod current-user nil [_]
  nil)

(defn
#^{ :doc "Returns true if the current user is an admin." }
  is-admin?
  ([] (is-admin? (current-user (logged-in?)))) 
  ([user]
    (let [is-admin (get user :is_admin)]
      (not (or (nil? is-admin) (= is-admin 0))))))

(defn
#^{ :doc "Logs the requestor out." }
  logout []
  (session-store/delete session-key))