(ns views.authentication.create-user
  (:use conjure.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]))

(defview [errors]
  (html/html 
    (when (and errors (not-empty errors))
      [:div
        "Please correct the following errors:"
        [:ul
          (map (fn [error] [:li (helpers/h error)]) errors)]])
    (form-for request-map { :action "save-user" }
      (html/htmli
        [:p "Create a user:"]
        [:div "User Name:&nbsp;" (text-field {} :user :name)]
        [:div "Password:&nbsp;" (password-field {} :user :password)]
        [:div "Verify Password:&nbsp;" (password-field {} :user :password-verify)]
        (form-button "Create")
        "&nbsp;"
        (link-back "Cancel" request-map)))))