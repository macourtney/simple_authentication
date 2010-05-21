(ns bindings.authentication.login
  (:use conjure.binding.base))

(defbinding [request-map]
  (let [params (:params request-map)
        errors (:errors params)]
    (render-view request-map (if errors (read-string errors)) (:back-link params))))