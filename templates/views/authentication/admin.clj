(ns views.authentication.admin
  (:use conjure.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]))

(defn
  user-row [user]
  [:tr
    [:td (helpers/h (:id user))]
    [:td (helpers/h (:name user))]
    [:td (helpers/h (:is-admin user))]])

(defview [users]
  (html/html 
    [:div 
      [:h2 "Current Users"]
      [:table
        [:tr
          [:th "Id"]
          [:th "Name"]
          [:th "Is Admin"]]
        (map user-row users)]]))