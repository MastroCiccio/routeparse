(ns routeparse.routes
  (:require [compojure.core :refer [defroutes POST GET context]]
            [routeparse.exports :as exports]))

(defroutes
  something
  (GET "/some/thing" [] (+ 1 1))
  (GET "/some/thing2" [] (+ 1 1)))

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