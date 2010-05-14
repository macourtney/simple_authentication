(ns plugins.simple-authentication.plugin
  (:import [java.io File])
  (:use conjure.plugin.base)
  (:require [clojure.contrib.duck-streams :as duck-streams]
            [clojure.contrib.logging :as logging]
            [conjure.plugin.util :as plugin-util]
            [conjure.model.util :as model-util]
            [conjure.test.util :as test-util]
            [conjure.util.file-utils :as file-utils]
            [destroyers.migration-destroyer :as migration-destroyer]
            [generators.migration-generator :as migration-generator]))

(def model-name "user")
(def migration-name "create-users")

(def templates-directory-name "templates")
(def models-directory-name "models")
(def test-directory-name "test")
(def fixture-directory-name "fixture")
(def unit-directory-name "unit")
(def model-test-directory-name "model")
(def user-model-name "user")
(def user-model-file-name (str user-model-name ".clj"))
(def fixture-file-name (str user-model-name ".clj"))
(def model-unit-test-file-name (str user-model-name "_model_test.clj"))

(def simple-authentication-dir (plugin-util/plugin-directory (plugin-name)))
(def templates-directory (file-utils/find-directory simple-authentication-dir templates-directory-name))
(def template-models-directory (file-utils/find-directory templates-directory models-directory-name))
(def template-test-directory (file-utils/find-directory templates-directory test-directory-name))
(def template-test-fixture-directory (file-utils/find-directory template-test-directory fixture-directory-name))
(def template-test-unit-directory (file-utils/find-directory template-test-directory unit-directory-name))
(def template-test-unit-model-directory
  (file-utils/find-directory template-test-unit-directory model-test-directory-name))

(def user-model-file (file-utils/find-file template-models-directory user-model-file-name))
(def fixture-file (file-utils/find-directory template-test-fixture-directory fixture-file-name))
(def unit-test-file (file-utils/find-directory template-test-unit-model-directory model-unit-test-file-name))

(def generated-user-model-file (File. (model-util/find-models-directory) user-model-file-name))
(def generated-fixture-file (File. (test-util/find-fixture-directory) fixture-file-name))
(def generated-model-test-file (File. (test-util/find-model-unit-test-directory) model-unit-test-file-name))

(defn migration-up-content []
  (str "(create-table \"" (model-util/model-to-table-name model-name) "\" 
    (id)
    (string :name)
    (string :password)
    (integer :is-admin))"))

(defn migration-down-content []
  (str "(drop-table \"" (model-util/model-to-table-name model-name) "\")"))

(defn
#^{ :doc "Copies source file to the destination file and logs the creation of the destination file.." }
  log-and-copy-file [source-file destination-file]
  (logging/info (str "Creating file: " (.getPath destination-file) "..."))
  (duck-streams/copy source-file destination-file))

(defn
#^{ :doc "Copies the user.clj model file to the models directory." }
  copy-model-file []
  (log-and-copy-file user-model-file generated-user-model-file))

(defn
#^{ :doc "Copies the user.clj fixture file to the test fixture directory." }
  copy-fixture-file []
  (log-and-copy-file fixture-file generated-fixture-file))

(defn
#^{ :doc "Copies the user_model_test.clj file to the model test directory." }
  copy-model-test-file []
  (log-and-copy-file unit-test-file generated-model-test-file))

(defn
#^{ :doc "Logs the deletion of the given file." }
  log-and-delete-file [file]
  (logging/info (str "Deleting file: " (.getPath file) "..."))
  (.delete file))

(defn
#^{ :doc "Deletes the generated user model file. For use by uninstall." }
  delete-model-file []
  (log-and-delete-file generated-user-model-file))

(defn
#^{ :doc "Deletes the generated fixture file. For use by uninstall." }
  delete-fixture-file []
  (log-and-delete-file generated-fixture-file))

(defn
#^{ :doc "Deletes the generated model test file. For use by uninstall." }
  delete-model-test-file []
  (log-and-delete-file generated-model-test-file))

(defn install [arguments]
  (migration-generator/generate-migration-file migration-name (migration-up-content) (migration-down-content))
  (copy-model-file)
  (copy-fixture-file)
  (copy-model-test-file))

(defn uninstall [arguments]
  (migration-destroyer/destroy-migration-file migration-name)
  (delete-model-file)
  (delete-fixture-file)
  (delete-model-test-file))

(defn initialize [])