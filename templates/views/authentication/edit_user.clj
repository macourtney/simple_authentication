(ns views.authentication.edit-user
  (:use conjure.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]
            [views.authentication.errors :as errors]))

(defview [user errors]
  (html/html
    (errors/render-view request-map errors)
    [:div { :class "article" }
      [:h2 (str "Editing " (helpers/h (:name user)))]
      (form-for request-map { :name "edit-save", :action "edit-save" }
        (list
          (hidden-field user :user :id)
          [:p [:strong "Name"] ": " (text-field user :user :name)]
          [:p [:strong "Password"] ": " (password-field user :user :password)]
          [:p [:strong "Verify Password"] ": " 
            (password-field { :password-verify (:password user) } :user :password-verify)]
          [:p [:strong "Is Admin"] ": " 
            (select-tag user :user :is_admin 
              { :options [{ :name :no, :value "0" } { :name :yes, :value "1" }] })]
          (form-button "Save")
          "&nbsp;"
          (link-to "Cancel" request-map { :action "admin" } )))]))