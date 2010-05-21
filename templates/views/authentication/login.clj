(ns views.authentication.login
  (:use conjure.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]))

(defview [errors back-link]
  (html/html
    (when (and errors (not-empty errors))
      [:div
        "An error occured while logging in:"
        [:ul
          (map (fn [error] [:li (helpers/h error)]) errors)]])
    (form-for request-map { :action "login-check" }
      (html/htmli
        (hidden-field { :back-link (or back-link (back-url request-map)) } :login :back-link)
        [:p "Log In:"]
        [:div "User Name:&nbsp;" (text-field {} :user :name)]
        [:div "Password:&nbsp;" (password-field {} :user :password)]
        (form-button "Login")
        "&nbsp;"
        (if back-link
          [:a { :href back-link } "Cancel"]
          (link-back "Cancel" request-map))))))