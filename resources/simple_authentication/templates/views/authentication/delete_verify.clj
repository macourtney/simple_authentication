(ns views.authentication.delete-verify
  (:use conjure.core.view.base)
  (:require [hiccup.core :as hiccup]))

(def-view [user]
  [:div { :class "article" }
    [:h2 (str "Deleting " (hiccup/h (:name user)))]
    [:p "Are you sure you want to delete this user?"]
    [:p [:strong "Name"] ": " (hiccup/h (:name user))]
    (button-to "Delete" { :action "delete-user", :params { :id user } })
    "&nbsp;"
    (link-to "Cancel" { :action "admin" })])