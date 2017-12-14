(ns routeparse.core
  (:require [clojure.string :as st]
            [instaparse.core :as insta]))

(def r-parser
  (insta/parser "default-grammar.bnf" :auto-whitespace :standard))

(defn r-tranformation-map
  []
  (let []
    {:IGNORE (fn [_] "skipped")
     :METHOD identity
     :PATH identity
     :ARGS str
     :ROUTE (fn [& args] (st/join " " args))
     :HANDLER_NAME keyword
     :HANDLER (fn [name & routes] {name routes})
     }))

(def example
  (r-parser "(defroutes
                thename
                  (GET \"/path/of/uri\"
                    [x y]
                    (println x y))

                  #_(PUT \"/path/of/uri\"
                    [a b]
                    ((keyword a) (map () [0 1 2])))
                  )"))
example

(insta/transform (r-tranformation-map) example)
