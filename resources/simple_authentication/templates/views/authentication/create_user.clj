(ns views.authentication.create-user
  (:use conjure.core.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]
            [views.authentication.errors :as errors]))

(defview [errors]
  (html/html
    [:div { :class "article" }
      (errors/render-view request-map errors)
      [:h2 "Create a user"]
      (form-for request-map { :action "save-user" }
        (html/htmli
          [:div "User Name:&nbsp;" (text-field {} :user :name)]
          [:div "Password:&nbsp;" (password-field {} :user :password)]
          [:div "Verify Password:&nbsp;" (password-field {} :user :password-verify)]
          (form-button "Create")
          "&nbsp;"
          (link-to "Cancel" request-map { :action :login })))]))