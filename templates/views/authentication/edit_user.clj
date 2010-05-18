(ns views.authentication.edit-user
  (:use conjure.view.base)
  (:require [clj-html.core :as html]))

(defview []
  (html/html 
    [:p "You can change this text in app/views/authentication/edit_user.clj"]))