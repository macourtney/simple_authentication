(ns views.authentication.login
  (:use conjure.core.view.base)
  (:require [hiccup.core :as hiccup]))

(def-view [errors back-link]
  [:div { :class "article" }
    (when (and errors (not-empty errors))
      [:div
        "An error occured while logging in:"
        [:ul
          (map (fn [error] [:li (hiccup/h error)]) errors)]])
    [:h2 "Log In"]
    (form-for { :action "login-check" }
      (list
        (hidden-field { :back-link (or back-link (back-url)) } :login :back-link)
        [:div "User Name:&nbsp;" (text-field {} :user :name)]
        [:div "Password:&nbsp;" (password-field {} :user :password)]
        (form-button "Login")
        "&nbsp;"
        (if back-link
          [:a { :href back-link } "Cancel"])))])