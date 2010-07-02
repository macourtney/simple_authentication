(ns plugins.simple-authentication.plugin
  (:import [java.io File])
  (:use conjure.core.plugin.base)
  (:require [clojure.contrib.duck-streams :as duck-streams]
            [clojure.contrib.logging :as logging]
            [clojure.contrib.str-utils :as str-utils]
            [conjure.core.binding.util :as binding-util]
            [conjure.core.config.environment :as environment]
            [conjure.core.controller.util :as controller-util]
            [conjure.core.model.util :as model-util]
            [conjure.core.plugin.util :as plugin-util]
            [conjure.core.test.builder :as test-builder]
            [conjure.core.test.util :as test-util]
            [conjure.core.util.file-utils :as file-utils]
            [conjure.core.util.loading-utils :as loading-utils]
            [conjure.core.view.util :as view-util]
            [conjure.script.destroyers.migration-destroyer :as migration-destroyer]
            [conjure.script.generators.migration-generator :as migration-generator]))

(def model-name "user")
(def migration-name "create-users")
(def controller-name "authentication")

(def simple-authentication-directory-name "simple_authentication")
(def templates-directory-name "templates")
(def models-directory-name "models")
(def controllers-directory-name "controllers")
(def bindings-directory-name "bindings")
(def authentication-directory-name "authentication")
(def test-directory-name "test")
(def fixture-directory-name "fixture")
(def unit-directory-name "unit")
(def model-test-directory-name "model")
(def functional-directory-name "functional")
(def binding-test-directory-name "binding")
(def views-directory-name "views")
(def view-test-directory-name "view")
(def config-directory-name "config")

(def user-model-file-name (str model-name ".clj"))
(def fixture-file-name (str model-name ".clj"))
(def model-unit-test-file-name (str model-name "_model_test.clj"))
(def controller-file-name (str controller-name "_controller.clj"))
(def controller-test-file-name (str controller-name "_controller_test.clj"))
(def config-file-name "authentication_config.clj")

(def templates-directory (str simple-authentication-directory-name "/"  templates-directory-name))
(def template-models-directory (str templates-directory "/" models-directory-name))
(def template-controllers-directory (str templates-directory "/" controllers-directory-name))
(def template-bindings-directory (str templates-directory "/" bindings-directory-name))
(def template-bindings-authentication-directory (str template-bindings-directory "/" authentication-directory-name))
(def template-views-directory (str templates-directory "/" views-directory-name))
(def template-views-authentication-directory (str template-views-directory "/" authentication-directory-name))
(def template-test-directory (str templates-directory "/" test-directory-name))
(def template-test-fixture-directory (str template-test-directory "/" fixture-directory-name))
(def template-test-unit-directory (str template-test-directory "/" unit-directory-name))
(def template-test-unit-model-directory (str template-test-unit-directory "/" model-test-directory-name))
(def template-test-functional-directory (str template-test-directory "/" functional-directory-name))
(def template-test-unit-binding-directory (str template-test-unit-directory "/" binding-test-directory-name))
(def template-test-unit-binding-authentication-directory 
  (str template-test-unit-binding-directory "/" authentication-directory-name))
(def template-test-unit-view-directory (str template-test-unit-directory "/" view-test-directory-name))
(def template-test-unit-view-authentication-directory
  (str template-test-unit-view-directory "/" authentication-directory-name))
(def template-config-directory (str templates-directory "/" config-directory-name))

(def source-directories [templates-directory template-models-directory template-controllers-directory 
                         template-bindings-directory template-bindings-authentication-directory 
                         template-views-directory template-views-authentication-directory template-test-directory
                         template-test-fixture-directory template-test-unit-directory 
                         template-test-unit-model-directory template-test-functional-directory
                         template-test-unit-binding-directory template-test-unit-binding-authentication-directory
                         template-test-unit-view-directory template-test-unit-view-authentication-directory
                         template-config-directory])

(def user-model-file (str template-models-directory "/" user-model-file-name))
(def fixture-file (str template-test-fixture-directory "/" fixture-file-name))
(def unit-test-file (str template-test-unit-model-directory "/" model-unit-test-file-name))
(def controller-file (str template-controllers-directory "/" controller-file-name))
(def controller-test-file (str template-test-functional-directory "/" controller-test-file-name))
(def config-file (str template-config-directory "/" config-file-name))

(def source-files [user-model-file fixture-file unit-test-file controller-file controller-test-file config-file])

(def generated-user-model-file (File. (model-util/find-models-directory) user-model-file-name))
(def generated-fixture-file (File. (test-builder/find-or-create-fixture-directory false) fixture-file-name))
(def generated-model-test-file 
  (File. (test-builder/find-or-create-model-unit-test-directory false) model-unit-test-file-name))
(def generated-controller-file (File. (controller-util/find-controllers-directory) controller-file-name))
(def generated-controller-test-file 
  (File. (test-builder/find-or-create-functional-test-directory false) controller-test-file-name))
(def generated-config-file (File. (environment/find-source-dir) (str config-directory-name "/" config-file-name)))

(def generated-files [generated-user-model-file generated-fixture-file generated-model-test-file 
                      generated-controller-file generated-controller-test-file generated-config-file])

(defn migration-up-content []
  (str "(create-table \"" (model-util/model-to-table-name model-name) "\" 
    (id)
    (string :name)
    (string :password)
    (integer :is-admin))
  (insert-into \"" (model-util/model-to-table-name model-name) "\" { :name \"admin\", :password \"password\", :is_admin 1 })"))

(defn migration-down-content []
  (str "(drop-table \"" (model-util/model-to-table-name model-name) "\")"))

(defn
#^{ :doc "Copies source file to the destination file and logs the creation of the destination file." }
  log-and-copy-file [source-file destination-file]
  (logging/info (str "Creating file: " (.getPath destination-file) "..."))
  (let [source-file-file (if (string? source-file) (loading-utils/find-resource source-file) source-file)]
    (duck-streams/copy source-file-file destination-file)))

(defn
  source-dir-name [source-dir-str]
  (last (str-utils/re-split #"/" source-dir-str)))

(defn
#^{ :doc "Copies the entire source directory to the destination directory." }
  log-and-copy-directory [source-dir destination-dir]
  (when (and source-dir destination-dir)
    (let [source-dir-name (source-dir-name source-dir)
          created-directory (File. destination-dir source-dir-name)]
      (logging/info (str "Creating directory: " (.getPath created-directory) "..."))
      (.mkdirs created-directory)
      (doseq [sub-file-name (loading-utils/all-class-path-file-names source-dir)]
        (if (.endsWith sub-file-name ".clj")
          (log-and-copy-file (str source-dir "/"  sub-file-name) (File. created-directory sub-file-name))
          (log-and-copy-directory sub-file-name created-directory))))))

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
#^{ :doc "Copies all of the model files to the appropriate places." }
  create-model-files []
  (migration-generator/generate-migration-file migration-name (migration-up-content) (migration-down-content))
  (copy-model-file)
  (copy-fixture-file)
  (copy-model-test-file))

(defn
#^{ :doc "Copies the authentication_controller.clj file to the controllers directory." }
  copy-controller-file []
  (log-and-copy-file controller-file generated-controller-file))

(defn
#^{ :doc "Copies the authentication_controller_test.clj file to the functional test directory." }
  copy-controller-test-file []
  (log-and-copy-file controller-test-file generated-controller-test-file))

(defn
#^{ :doc "Copies all of the controller files to the appropriate places." }
  create-controller-files []
  (copy-controller-file)
  (copy-controller-test-file))

(defn
#^{ :doc "Copies the contents of the authentication bindings directory to the appropriate place." }
  copy-bindings-directory []
  (log-and-copy-directory template-bindings-authentication-directory (binding-util/find-bindings-directory)))

(defn
#^{ :doc "Copies the contents of the authentication view test directory to the appropriate place." }
  copy-binding-test-directory []
  (log-and-copy-directory template-test-unit-binding-authentication-directory
    (test-builder/find-or-create-binding-unit-test-directory false)))

(defn
#^{ :doc "Copies all of the bindings files to the appropriate places." }
  create-bindings-files []
  (copy-bindings-directory)
  (copy-binding-test-directory))

(defn
#^{ :doc "Copies the contents of the authentication view directory to the appropriate place." }
  copy-view-directory []
  (log-and-copy-directory template-views-authentication-directory (view-util/find-views-directory)))

(defn
#^{ :doc "Copies the contents of the authentication view test directory to the appropriate place." }
  copy-view-test-directory []
  (log-and-copy-directory 
    template-test-unit-view-authentication-directory
    (test-builder/find-or-create-view-unit-test-directory {})))

(defn
#^{ :doc "Copies all of the view files to the appropriate places." }
  create-view-files []
  (copy-view-directory)
  (copy-view-test-directory))

(defn
#^{ :doc "Copies all of the config files to the appropriate places." }
  create-config-files []
  (log-and-copy-file config-file generated-config-file))

(defn
#^{ :doc "Logs the deletion of the given file." }
  log-and-delete-file [file]
  (logging/info (str "Deleting file: " (.getPath file) "..."))
  (.delete file))

(defn
#^{ :doc "Logs the deletion of all of the files and directories in the destination directory which originated from the
given source directory." }
  log-and-delete-directory [source-dir destination-dir]
  (when source-dir
    (let [source-dir-name (source-dir-name source-dir)
          directory-to-delete (File. destination-dir source-dir-name)]
      (when (.exists directory-to-delete)
        (doseq [sub-file-name (loading-utils/all-class-path-file-names source-dir)]
          (if (.endsWith sub-file-name ".clj")
            (let [file-to-delete (File. directory-to-delete sub-file-name)]
              (when (.exists file-to-delete)
                (log-and-delete-file file-to-delete)))
            (log-and-delete-directory (str source-dir "/"  sub-file-name) directory-to-delete)))
        (when (empty? (.list directory-to-delete))
          (logging/info (str "Deleting directory: " (.getPath directory-to-delete) "..."))
          (.delete directory-to-delete))))))

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

(defn
#^{ :doc "Deletes all of the generated model files." }
  destroy-model-files []
  (migration-destroyer/destroy-migration-file migration-name)
  (delete-model-file)
  (delete-fixture-file)
  (delete-model-test-file))

(defn
#^{ :doc "Deletes the generated authentication controller file. For use by uninstall." }
  delete-controller-file []
  (log-and-delete-file generated-controller-file))

(defn
#^{ :doc "Deletes the generated authentication controller test file. For use by uninstall." }
  delete-controller-test-file []
  (log-and-delete-file generated-controller-test-file))

(defn
#^{ :doc "Deletes all of the generated controller files." }
  destroy-controller-files []
  (delete-controller-file)
  (delete-controller-test-file))

(defn
#^{ :doc "Deletes all of the generated bindings files. For use by uninstall." }
  delete-bindings-directory []
  (log-and-delete-directory template-bindings-authentication-directory (binding-util/find-bindings-directory)))

(defn
#^{ :doc "Deletes all of the generated view test files. For use by uninstall." }
  delete-binding-test-directory []
  (log-and-delete-directory template-test-unit-binding-authentication-directory
    (test-util/find-binding-unit-test-directory)))

(defn
#^{ :doc "Deletes all of the generated bindings files." }
  destroy-bindings-files []
  (delete-bindings-directory)
  (delete-binding-test-directory))

(defn
#^{ :doc "Deletes all of the generated view files. For use by uninstall." }
  delete-view-directory []
  (log-and-delete-directory template-views-authentication-directory (view-util/find-views-directory)))

(defn
#^{ :doc "Deletes all of the generated view test files. For use by uninstall." }
  delete-view-test-directory []
  (log-and-delete-directory template-test-unit-view-authentication-directory (test-util/find-view-unit-test-directory)))

(defn
#^{ :doc "Deletes all of the generated view files." }
  destroy-view-files []
  (delete-view-directory)
  (delete-view-test-directory))

(defn
#^{ :doc "Deletes all of the generated config files." }
  destroy-config-files []
  (log-and-delete-file generated-config-file))

(defn
#^{ :doc "Returns true if this plugin has likely been installed." }
  installed? []
  (.exists generated-config-file))

(defn install [arguments]
  (create-model-files)
  (create-controller-files)
  (create-bindings-files)
  (create-view-files)
  (create-config-files))

(defn uninstall [arguments]
  (destroy-model-files)
  (destroy-controller-files)
  (destroy-bindings-files)
  (destroy-view-files)
  (destroy-config-files))

(defn initialize []
  (when (installed?)
    (require 'authentication-config)
    ((ns-resolve 'authentication-config 'configure))))