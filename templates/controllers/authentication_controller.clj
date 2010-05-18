(ns controllers.authentication-controller
  (:use [conjure.controller.base]))

(defaction login
  (bind request-map))

(defaction logout
  (bind request-map))

(defaction create-user
  (bind request-map))

(defaction admin
  (bind request-map))

(defaction delete-user
  (bind request-map))

(defaction edit-user
  (bind request-map))