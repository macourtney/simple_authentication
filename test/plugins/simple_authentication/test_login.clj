(ns plugins.simple-authentication.test-login
  (:use clojure.contrib.test-is)
  (:require [plugins.simple-authentication.plugin :as plugin]
            [conjure.core.server.server :as server]))

(server/init)

(plugin/install []) ; Create the user model file.

(use 'plugins.simple-authentication.login)

(defn init-plugin [test-fn]
  (try
    (test-fn)
    (finally
      (plugin/uninstall []))))

(use-fixtures :once init-plugin)

(deftest test-is-admin?
  (is (is-admin? { :is_admin 1 }))
  (is (not (is-admin? { :is_admin 0 })))
  (is (not (is-admin? {})))
  (is (not (is-admin? nil))))