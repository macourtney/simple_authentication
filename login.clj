(ns plugins.simple-authentication.login
  (:import [clojure.lang PersistentArrayMap]) 
  (:require [clojure.contrib.logging :as logging] 
            [models.user :as user]
            session-config))

(def session-key :user-id) 

(defn
#^{ :doc "Logs the given user into the system and returns the user. If the user does not exist or the password is
incorrect this function returns nil." }
  login [request-map user-name user-password]
  (let [user (user/find-user user-name user-password)]
    (when user
      ((:save session-config/session-store) request-map session-key (:id user))
      user)))

(defn
#^{ :doc "Returns true if the current user is an admin." }
  is-admin? [user]
  (let [is-admin (get user :is_admin)]
    (and (not (or (nil? is-admin) (= is-admin 0))))))

(defn
#^{ :doc "If the requestor is logged in, then this function returns the user id. Otherwise, this function returns 
nil" }
  logged-in? [request-map] 
  (session-key ((:retrieve session-config/session-store) request-map)))

(defmulti current-user type)
 
(defmethod current-user PersistentArrayMap [request-map]
  (current-user (logged-in? request-map))) 

(defmethod current-user String [user-id]
  (when user-id
    (user/get-record { :id user-id }))) 

(defmethod current-user Integer [user-id]
  (when user-id
    (user/get-record user-id)))

(defmethod current-user nil [_]
  nil) 

(defn
#^{ :doc "Logs the requestor out." }
  logout [request-map]
  ((:delete session-config/session-store) request-map session-key))