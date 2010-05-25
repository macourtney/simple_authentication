(ns views.authentication.create-user
  (:use conjure.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]
            [views.authentication.errors :as errors]))

(defview [errors]
  (html/html 
    (errors/render-view request-map errors)
    (form-for request-map { :action "save-user" }
      (html/htmli
        [:p "Create a user:"]
        [:div "User Name:&nbsp;" (text-field {} :user :name)]
        [:div "Password:&nbsp;" (password-field {} :user :password)]
        [:div "Verify Password:&nbsp;" (password-field {} :user :password-verify)]
        (form-button "Create")
        "&nbsp;"
        (link-back "Cancel" request-map)))))