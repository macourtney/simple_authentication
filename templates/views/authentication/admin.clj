(ns views.authentication.admin
  (:use conjure.view.base)
  (:require [clj-html.core :as html]
            [clj-html.helpers :as helpers]
            [clojure.contrib.logging :as logging]))

(defn
  user-row [request-map user]
  (logging/debug (str "user: " user))
  [:tr
    [:td (helpers/h (:id user))]
    [:td (helpers/h (:name user))]
    [:td (helpers/h (if (= (:is_admin user) 0) "no" "yes"))]
    [:td (link-to "edit" request-map { :action "edit-user", :params { :id user } })]
    [:td (link-to "delete" request-map { :action "delete-verify", :params { :id user } })]])

(defview [users]
  (html/html 
    [:div 
      [:h2 "Current Users"]
      [:table
        [:tr
          [:th "Id"]
          [:th "Name"]
          [:th "Is Admin"]
          [:th]
          [:th]]
        (map #(user-row request-map %1) users)]]))