(ns plugins.simple-authentication.test-login
  (:use clojure.contrib.test-is)
  (:require [plugins.simple-authentication.plugin :as plugin]))

(plugin/install []) ; Create the user model file.

(use 'plugins.simple-authentication.login) ; Must not be used before the user model file is created.

(defn init-plugin [test-fn]
  (test-fn)
  (plugin/uninstall []))

(use-fixtures :once init-plugin)

(deftest test-is-admin?
  (is (is-admin? { :is_admin 1 }))
  (is (not (is-admin? { :is_admin 0 })))
  (is (not (is-admin? {})))
  (is (not (is-admin? nil))))