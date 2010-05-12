(ns plugins.simple-authentication.plugin
  (:require [conjure.model.util :as model-util]
            [destroyers.migration-destroyer :as migration-destroyer]
            [generators.migration-generator :as migration-generator]))

(def model-name "user")
(def migration-name "create-users")

(defn migration-up-content []
  (str "(create-table \"" (model-util/model-to-table-name model-name) "\" 
    (id)
    (string :name)
    (string :password))"))

(defn migration-down-content []
  (str "(drop-table \"" (model-util/model-to-table-name model-name) "\")"))

(defn install [arguments]
  (migration-generator/generate-migration-file migration-name (migration-up-content) (migration-down-content)))

(defn uninstall [arguments]
  (migration-destroyer/destroy-migration-file migration-name))

(defn initialize [])