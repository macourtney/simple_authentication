(ns views.authentication.access-denied
  (:use conjure.core.view.base))

(def-view []
  [:div { :class "article" }
    [:p "You do not have authorization to access that page."]])