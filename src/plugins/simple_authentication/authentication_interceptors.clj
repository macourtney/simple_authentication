(ns plugins.simple-authentication.authentication-interceptors
  (:require [clojure.contrib.logging :as logging] 
            [conjure.core.controller.base :as controller-base] 
            [conjure.core.controller.util :as controller-util]
            [conjure.core.server.request :as request]
            [plugins.simple-authentication.login :as login]))

(defn
#^{ :doc "An interceptor which tests if the user is logged in. If a user is not logged in, then this interceptor
redirects the request to the login page." }
  login-interceptor [action]
  (if (login/logged-in?)
    (action)
    (controller-base/redirect-to { :controller "authentication", :action "login" }))) 

(defn
#^{ :doc "Adds the login interceptor to the list of app interceptors with the given excludes map. The excludes map
should include any controller and action you wish not to put behind a login which includes the login and create account
pages." }
  init-login-interceptor [excludes]
  (controller-util/add-app-interceptor login-interceptor excludes))

(defn
#^{ :doc "Calls the given action with the given request-map if authorization is true. Otherwise, this function redirects
the request to the access denied page." }
  authorize-access [action authorization]
  (if authorization
    (action)
    (controller-base/redirect-to { :controller "authentication", :action "access-denied" })))

(defn
#^{ :doc "An interceptor which tests if the user is an admin. If a user is not an admin, then this interceptor
redirects the request to the access denied page." }
  admin-interceptor [action]
  (authorize-access action (login/is-admin?))) 

(defn
#^{ :doc "Adds the given interceptor only for the given includes. Includes is a map, mapping controllers to the included
action set for that controller. For example, to include the action 'admin' in controller 'authentication' the includes
map would look like { :authentication #{ :admin } }." }
  add-interceptor-with-includes [interceptor interceptor-name includes]
  (when (and includes (not-empty includes))
    (doseq [controller-action-pair includes]
      (let [controller (first controller-action-pair)
            include-actions (second controller-action-pair)]
        (controller-util/add-interceptor interceptor interceptor-name controller nil include-actions))))) 

(defn
#^{ :doc "Adds the admin interceptor to the list of interceptors with the given includes map. Includes is a map, mapping
controllers to the included action set for that controller. For example, to include the action 'admin' in controller
'authentication' the includes map would look like { :authentication #{ :admin } }" }
  init-admin-interceptor [includes]
  (add-interceptor-with-includes admin-interceptor :admin includes))

(defn
#^{ :doc "An interceptor which tests if the user is the same as the request-map id or an admin. If a user is not either,
then this interceptor redirects the request to the access denied page." }
  admin-or-user-interceptor [action]
  (let [user-id (login/logged-in?)]
    (authorize-access action
      (or (= (str user-id) (request/id)) (login/is-admin? (login/current-user user-id))))))

(defn
#^{ :doc "Adds the admin or user interceptor to the list of interceptors with the given includes map. Includes is a map,
mapping controllers to the included action set for that controller. For example, to include the action 'edit-user' in
controller 'authentication' the includes map would look like { :authentication #{ :edit-user } }" }
  init-admin-or-user-interceptor [includes]
  (add-interceptor-with-includes admin-or-user-interceptor :admin-or-user includes))