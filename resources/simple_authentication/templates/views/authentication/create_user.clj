(ns views.authentication.create-user
  (:use conjure.core.view.base)
  (:require [views.authentication.errors :as errors]))

(def-view [errors]
  [:div { :class "article" }
    (errors/render-view errors)
    [:h2 "Create a user"]
    (form-for { :action "save-user" }
      (list
        [:div "User Name:&nbsp;" (text-field {} :user :name)]
        [:div "Password:&nbsp;" (password-field {} :user :password)]
        [:div "Verify Password:&nbsp;" (password-field {} :user :password-verify)]
        (form-button "Create")
        "&nbsp;"
        (link-to "Cancel" { :action :login })))])