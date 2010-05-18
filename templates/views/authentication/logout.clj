(ns views.authentication.logout
  (:use conjure.view.base)
  (:require [clj-html.core :as html]))

(defview []
  (html/html 
    [:p "You can change this text in app/views/authentication/logout.clj"]))