(ns views.authentication.errors
  (:use conjure.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]
            [clojure.contrib.seq-utils :as seq-utils]))

(defn error-item [error]
  [:li (helpers/h error)]) 

(defn
#^{ :doc "Converts the given errors string into an error sequence." }
  error-list [errors]
  (let [error-map (if (string? errors) (read-string errors) errors)]
    (when (and error-map (map? error-map))
      (seq-utils/flatten (vals error-map))))) 

(defview [errors]
  (html/html 
    (when (and errors (not-empty errors))
      [:div
        "Please correct the following errors:"
        [:ul
          (map error-item (error-list errors))]])))