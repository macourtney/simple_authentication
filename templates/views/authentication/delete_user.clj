(ns views.authentication.delete-user
  (:use conjure.view.base)
  (:require [clj-html.core :as html]))

(defview []
  (html/html 
    [:p "You can change this text in app/views/authentication/delete_user.clj"]))