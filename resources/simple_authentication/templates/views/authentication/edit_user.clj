(ns views.authentication.edit-user
  (:use conjure.core.view.base)
  (:require [hiccup.core :as hiccup]
            [plugins.simple-authentication.login :as login]
            [views.authentication.errors :as errors]))

(def-view [user errors]
  [:div { :class "article" }
    (errors/render-view errors)
    [:h2 (str "Editing " (hiccup/h (:name user)))]
    (form-for { :name "edit-save", :action "edit-save" }
      (list
        (hidden-field user :user :id)
        [:p [:strong "Name"] ": " (text-field user :user :name)]
        [:p [:strong "Password"] ": " (password-field user :user :password)]
        [:p [:strong "Verify Password"] ": " 
          (password-field { :password-verify (:password user) } :user :password-verify)]
        (if (login/is-admin?)
          [:p [:strong "Is Admin"] ": " 
            (select-tag user :user :is_admin 
              { :options [{ :name :no, :value "0" } { :name :yes, :value "1" }] })])
        (form-button "Save")
        "&nbsp;"
        (link-to "Cancel" { :action "admin" } )))])