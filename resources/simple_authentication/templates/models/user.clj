(ns models.user
  (:use conjure.core.model.base
        clj-record.boot)
  (:require [clojure.contrib.logging :as logging]))

(def minimum-user-name-length 3)
(def minimum-password-length 6)

(defn
#^{ :doc "Returns a function which verifies the string passed to it is not nil and is at least length long." }
  verify-str-length [length]
  (fn [string] 
    (and string (>= (count string) length))))

(defn
#^{ :doc "Called right before a user is saved. Makes sure only :id, :user, :password, and :is-admin are in the user." }
  user-before-save [user]
  (select-keys user [:id :name :password :is_admin]))

(clj-record.core/init-model
  (:validation
    (:name 
      (str "User name must be at least " minimum-user-name-length " characters long.")
      (verify-str-length minimum-user-name-length))
    (:password 
      (str "Password must be at least " minimum-password-length " characters long.")
      (verify-str-length minimum-password-length)))
  (:callbacks
    (:before-save user-before-save)))

(defn
#^{ :doc "Returns true if both password and password-verify are not nil and equal to each other." }
  verify-password [user]
  (let [password (:password user)
        password-verify (:password-verify user)]
    (and password password-verify (= password password-verify))))

(defn
#^{ :doc "Returns a list of errors if the given user is not valid. Otherwise, this function returns nil or an empty 
sequence." }
  full-verify-user [user]
  (let [errors (validate user)]
    (if (verify-password user)
      errors
      (assoc errors :password-verify "The passwords you entered do not match."))))

(defn
#^{ :doc "Returns the user with the given name and password or nil if no user exists." }
  find-user [name password]
  (find-record { :name name, :password password}))