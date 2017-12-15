(ns routeparse.exports
  (:require [compojure.core :refer [defroutes POST GET context]]))

(defroutes
  ex-exports
  (GET "/export1" []))
