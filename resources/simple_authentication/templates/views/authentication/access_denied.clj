(ns views.authentication.access-denied
  (:use conjure.core.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]))

(defview []
  (html/html
    [:div { :class "article" }
      [:p "You do not have authorization to access that page."]]))