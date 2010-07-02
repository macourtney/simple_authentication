(ns views.authentication.delete-verify
  (:use conjure.core.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]))

(defview [user]
  (html/html 
    [:div { :class "article" }
      [:h2 (str "Deleting " (helpers/h (:name user)))]
      [:p "Are you sure you want to delete this user?"]
      [:p [:strong "Name"] ": " (helpers/h (:name user))]
      (button-to "Delete" request-map { :action "delete-user", :params { :id user } })
      "&nbsp;"
      (link-to "Cancel" request-map { :action "admin" })]))