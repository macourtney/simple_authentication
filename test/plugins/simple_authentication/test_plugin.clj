(ns plugins.simple-authentication.test-plugin
  (:import [java.io File])
  (:use clojure.contrib.test-is
        plugins.simple-authentication.plugin)
  (:require [conjure.core.binding.util :as binding-util]
            [drift.core :as migration-util]
            [conjure.core.test.builder :as test-builder]
            [conjure.core.util.file-utils :as file-utils]
            [conjure.core.util.loading-utils :as loading-utils]
            [conjure.core.util.string-utils :as string-utils]
            [conjure.core.view.util :as view-util]))

(deftest test-migration-up-content
  (is (migration-up-content)))

(deftest test-migration-down-content
  (is (migration-down-content)))

(deftest test-source-directories
  (doseq [source-directory source-directories]
    (is source-directory)
    (is (loading-utils/resource-exists? source-directory))))

(deftest test-source-files
  (doseq [source-file source-files]
    (is source-file)
    (is (loading-utils/resource-exists? source-file))))

(deftest test-generated-files
  (doseq [generated-file generated-files]
    (is generated-file)
    (is (instance? File generated-file))
    (is (.getParent generated-file) (str "No Parent directory for: " (.getPath generated-file)))))

(deftest test-log-and-copy-file
  (is (not (.exists generated-user-model-file)))
  (log-and-copy-file user-model-file generated-user-model-file)
  (is (.exists generated-user-model-file))
  (is (.delete generated-user-model-file)))

(deftest test-log-and-copy-directory
  (let [bindings-dir (binding-util/find-bindings-directory)
        destination-dir (File. bindings-dir "authentication")]
    (is bindings-dir)
    (is (.exists bindings-dir))
    (is (not (.exists destination-dir)))
    (log-and-copy-directory template-bindings-authentication-directory (binding-util/find-bindings-directory))
    (is (.exists destination-dir))
    (is (file-utils/recursive-delete destination-dir))))

(defn file-does-not-exist-error [file]
  (str "File " (.getPath file) " does not exist."))

(defn file-exists? [file]
  (is (.exists file) (file-does-not-exist-error file)))

(defn files-exist? [files]
  (doseq [file files]
    (is (.exists file) (file-does-not-exist-error file))))

(defn file-does-not-exist? [file]
  (is (not (.exists file)) (str "File " (.getPath file) " exists.")))

(defn files-not-exist? [files]
  (doseq [file files]
    (file-does-not-exist? file)))

(defn test-copy-and-delete-file [generated-file copy-file-fn delete-file-fn]
  (file-does-not-exist? generated-file)
  (copy-file-fn)
  (file-exists? generated-file)
  (delete-file-fn)
  (file-does-not-exist? generated-file))

(defn test-create-and-destroy-files [generated-files create-files-fn destroy-files-fn]
  (files-not-exist? generated-files)
  (create-files-fn)
  (files-exist? generated-files)
  (destroy-files-fn)
  (files-not-exist? generated-files))

(deftest test-copy-and-delete-model-file
  (test-copy-and-delete-file generated-user-model-file copy-model-file delete-model-file))

(deftest test-copy-and-delete-fixture-file
  (test-copy-and-delete-file generated-fixture-file copy-fixture-file delete-fixture-file))

(deftest test-copy-and-delete-model-test-file
  (test-copy-and-delete-file generated-model-test-file copy-model-test-file delete-model-test-file))

(deftest test-create-model-files
  (let [next-migrate-number (migration-util/find-next-migrate-number)
        migration-file-name (str (string-utils/prefill (str next-migrate-number) 3 "0") "_" 
                              (loading-utils/dashes-to-underscores migration-name) ".clj")
        file-list [(File. (migration-util/find-migrate-directory) migration-file-name) generated-user-model-file 
                   generated-fixture-file generated-model-test-file]]
    (test-create-and-destroy-files file-list create-model-files destroy-model-files)))

(deftest test-copy-and-delete-controller-file
  (test-copy-and-delete-file generated-controller-file copy-controller-file delete-controller-file))

(deftest test-copy-and-delete-controller-test-file
  (test-copy-and-delete-file generated-controller-test-file copy-controller-test-file delete-controller-test-file))

(deftest test-create-and-destroy-controller-files
  (test-create-and-destroy-files [generated-controller-file generated-controller-test-file] create-controller-files
    destroy-controller-files))

(deftest test-copy-and-delete-bindings-directory
  (test-copy-and-delete-file 
    (File. (binding-util/find-bindings-directory) (source-dir-name template-bindings-authentication-directory))
    copy-bindings-directory delete-bindings-directory))

(deftest test-copy-and-delete-binding-test-directory
  (test-copy-and-delete-file 
    (File. (test-builder/find-or-create-binding-unit-test-directory false)
      (source-dir-name template-test-unit-binding-authentication-directory))
    copy-binding-test-directory delete-binding-test-directory))

(deftest test-create-and-destroy-bindings-files
  (test-create-and-destroy-files 
    [(File. (binding-util/find-bindings-directory) (source-dir-name template-bindings-authentication-directory))
     (File. (test-builder/find-or-create-binding-unit-test-directory false)
       (source-dir-name template-test-unit-binding-authentication-directory))]
    create-bindings-files
    destroy-bindings-files))

(deftest test-copy-and-delete-view-directory
  (test-copy-and-delete-file 
    (File. (view-util/find-views-directory) (source-dir-name template-views-authentication-directory))
    copy-view-directory delete-view-directory))

(deftest test-copy-and-delete-view-test-directory
  (test-copy-and-delete-file 
    (File. (test-builder/find-or-create-view-unit-test-directory {})
      (source-dir-name template-test-unit-view-authentication-directory))
    copy-view-test-directory delete-view-test-directory))

(deftest test-create-and-destroy-view-files
  (test-create-and-destroy-files 
    [(File. (view-util/find-views-directory) (source-dir-name template-views-authentication-directory))
     (File. (test-builder/find-or-create-view-unit-test-directory {})
      (source-dir-name template-test-unit-view-authentication-directory))]
    create-view-files
    destroy-view-files))

(deftest test-copy-and-delete-config-files
  (test-copy-and-delete-file generated-config-file create-config-files destroy-config-files))

(deftest test-install-and-uninstall
  (install [])
  (is (installed?))
  (uninstall [])
  (is (not (installed?))))