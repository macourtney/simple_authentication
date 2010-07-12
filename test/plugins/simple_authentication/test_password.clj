(ns plugins.simple-authentication.test-password
  (:import [java.security MessageDigest]
           [java.util Random])
  (:use clojure.contrib.test-is
        plugins.simple-authentication.password))

(deftest test-create-salt
  (is (create-salt)))

(deftest test-byte-to-hex
  (is (= "00" (byte-to-hex 0)))
  (is (= "0f" (byte-to-hex 15)))
  (is (= "ff" (byte-to-hex 255))))

(deftest test-convert-to-hex
  (is (= "0000" (convert-to-hex (list 0 0))))
  (is (= "ffff" (convert-to-hex (list 255 255)))))

(deftest test-encrypt-password-string
  (is (encrypt-password-string "password" (create-salt))))