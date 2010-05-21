(ns plugins.simple-authentication.login
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
#^{ :doc "If the requestor is logged in,m then this function returns the user id. Otherwise, this function returns 
nil" }
  logged-in? [request-map] 
  (session-key ((:retrieve session-config/session-store) request-map)))

(defn
#^{ :doc "Logs the requestor out." }
  logout [request-map]
  ((:delete session-config/session-store) request-map session-key))