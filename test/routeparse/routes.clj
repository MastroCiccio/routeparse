(ns routeparse.routes
  (:require [compojure.core :refer [defroutes POST GET PUT context]]
            [routeparse.exports :as exports]))

(defroutes
  something
  (GET "/some/thing" [] (+ 1 1))
  (GET "/some/thing2" [] (+ 1 1)))

(defroutes
  only-routes
  (GET "/path/of/uri0"
       [x y]
    (println x y))

  (POST "/path/of/uri1"
        [a b]
    ((keyword a) (map inc [0 1 2])))

  (PUT "/path/of/uri2"
       []
    "body"))

(defroutes
  ex-routes
  (GET "/path/of/uri"
       [x y]
    (println x y))

  (POST "/path/of/uri"
        [a b]
    ((keyword a) (map inc [0 1 2])))

  (context "/manage" [] something)

  exports/ex-exports)