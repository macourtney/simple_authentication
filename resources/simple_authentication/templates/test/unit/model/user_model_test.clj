(ns unit.model.user-model-test
  (:use clojure.contrib.test-is
        models.user
        fixture.user))

(def model "user")

(use-fixtures :once fixture)

(deftest test-first-record
  (is (get-record 1)))

(deftest test-verify-str-length
  (is ((verify-str-length 3) "blah"))
  (is ((verify-str-length 3) "bla"))
  (is (not ((verify-str-length 3) "bl")))
  (is (not ((verify-str-length 3) "")))
  (is (not ((verify-str-length 3) nil))))

(deftest test-before-save
  (is (= (first records) (before-save (assoc (first records) :password-verify "password")))))

(deftest test-verify-password
  (is (verify-password { :password "password", :password-verify "password" }))
  (is (not (verify-password { :password "password", :password-verify "fail" })))
  (is (not (verify-password { :password "password" })))
  (is (not (verify-password { :password-verify "fail" })))
  (is (not (verify-password {}))))

(deftest test-full-verify-user
  (let [test-record (assoc (first records) :password-verify "password")]
    (is (= {} (full-verify-user test-record)))
    (is (= 1 (count (full-verify-user (assoc test-record :name "a")))))
    (is (= 1 (count (full-verify-user (assoc test-record :name "")))))
    (is (= 1 (count (full-verify-user (dissoc test-record :name)))))
    (is (= 2 (count (full-verify-user (assoc test-record :password "a")))))
    (is (= 2 (count (full-verify-user (assoc test-record :password "")))))
    (is (= 2 (count (full-verify-user (dissoc test-record :password)))))
    (is (= 1 (count (full-verify-user (dissoc test-record :password-verify)))))))