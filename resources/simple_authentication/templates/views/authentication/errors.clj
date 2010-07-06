(ns views.authentication.errors
  (:use conjure.core.view.base)
  (:require [hiccup.core :as hiccup]
            [clojure.contrib.seq-utils :as seq-utils]))

(defn error-item [error]
  [:li (hiccup/h error)]) 

(defn
#^{ :doc "Converts the given errors string into an error sequence." }
  error-list [errors]
  (let [error-map (if (string? errors) (read-string errors) errors)]
    (when (and error-map (map? error-map))
      (seq-utils/flatten (vals error-map))))) 

(def-view [errors]
  (when (and errors (not-empty errors))
    [:div
      "Please correct the following errors:"
      [:ul
        (map error-item (error-list errors))]]))