(ns routeparse.core-test
  (:require [clojure.test :refer :all]
            [routeparse.core :refer :all]
            [routeparse.utils :refer :all]
            [routeparse.routes :refer :all]
            [routeparse.exports :refer :all]))

(deftest a-test
  (testing "a-test"
    (is (= (parse "routeparse/routes.clj" 'ex-routes)
           {:ex-routes '("GET /export1 []"
                         "GET /manage/some/thing []"
                         "GET /manage/some/thing2 []"
                         "POST /path/of/uri [a b]"
                         "GET /path/of/uri [x y]")}))))
