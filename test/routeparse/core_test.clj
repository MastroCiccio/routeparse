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

(deftest core-test
  (testing "Testing parser with default grammar"
    (is (= (parse "routeparse/routes.clj" 'ex-routes)
           []))
    (is (= (transform-with [:HANDLER
                            [:HANDLER_NAME "ex-routes"]
                            [:ROUTE [:METHOD "GET"] [:PATH "/path/of/uri"] [:ARGS "[" "x y" "]"]]
                            [:ROUTE [:METHOD "POST"] [:PATH "/path/of/uri"] [:ARGS "[" "a b" "]"]]
                            [:ROUTE [:CONTEXT [:PATH "/manage"] " something"]]
                            [:ROUTE [:IMPORT "exports" "ex-exports"]]])
           {})))
  (testing "Testing default transform map"
    (is (= (parse-and-transform "routeparse/routes.clj" 'ex-routes)
           {:ex-routes '("GET /export1 []"
                         "GET /manage/some/thing []"
                         "GET /manage/some/thing2 []"
                         "POST /path/of/uri [a b]"
                         "GET /path/of/uri [x y]")}))))
