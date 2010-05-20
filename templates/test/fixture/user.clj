(ns fixture.user
  (:use conjure.model.database))

(def records [
  { :id 1
    :name "admin"
    :password "password" 
    :is_admin 1 }])

(defn fixture [function]
  (apply insert-into :users records)
  (function)
  (delete :users [ "true" ]))