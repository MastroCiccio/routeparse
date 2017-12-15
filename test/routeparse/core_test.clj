(ns routeparse.core-test
  (:require [clojure.test :refer :all]
            [routeparse.core :refer :all]
            [routeparse.utils :refer :all]
            [routeparse.routes :refer :all]
            [routeparse.exports :refer :all]))

(deftest utils-test
  (testing "Testing from-path and from-sym"
    (is (= (from-path "path/to/namespace.clj")
           'path.to.namespace))
    (is (= (from-sym 'path.to.namespace)
           "path/to/namespace.clj"))))

(deftest a-test
  (testing "a-test"
    (is (= (parse "routeparse/routes.clj" 'ex-routes)
           {:ex-routes '("GET /export1 []"
                         "GET /manage/some/thing []"
                         "GET /manage/some/thing2 []"
                         "POST /path/of/uri [a b]"
                         "GET /path/of/uri [x y]")}))))
