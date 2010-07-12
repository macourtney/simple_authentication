(ns plugins.simple-authentication.password
  (:import [java.security MessageDigest]
           [java.util Random])
  (:require [clojure.contrib.str-utils :as str-utils]))

(defn
  create-salt []
  (.nextInt (new Random))) 

(defn
  byte-to-hex [byte]
  (let [byte-hex (Integer/toHexString byte)]
    (if (< (count byte-hex) 2)
      (str "0" byte-hex)
      byte-hex)))

(defn
  convert-to-hex [data]
  (str-utils/str-join "" (map byte-to-hex data)))

(defn
  encrypt-password-string [password salt]
  (let [message-digest (MessageDigest/getInstance "SHA-1")
        password-and-salt (str password salt)]
    (.update message-digest (.getBytes password-and-salt "iso-8859-1"))
    (convert-to-hex (.digest message-digest))))