(ns views.authentication.admin
  (:use conjure.core.view.base)
  (:require [hiccup.core :as hiccup]
            [clojure.contrib.logging :as logging]
            [plugins.simple-authentication.login :as login]))

(defn
  user-row [user]
  [:tr
    [:td (hiccup/h (:id user))]
    [:td (hiccup/h (:name user))]
    [:td (hiccup/h (if (login/is-admin? user) "yes" "no"))]
    [:td (link-to "edit" { :action "edit-user", :params { :id user } })]
    [:td (link-to "delete" { :action "delete-verify", :params { :id user } })]])

(def-view [users]
  [:div { :class "article" }
    [:h2 "Current Users"]
    [:table
      [:tr
        [:th "Id"]
        [:th "Name"]
        [:th "Is Admin"]
        [:th]
        [:th]]
      (map user-row users)]])