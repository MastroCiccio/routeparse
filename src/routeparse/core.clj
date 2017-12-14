(ns routeparse.core
  (:require [clojure.string :as st]
            [instaparse.core :as insta]
            [routeparse.utils :as utils]))

(def r-parser
  (insta/parser "default-grammar.bnf" :auto-whitespace :standard))

(defn r-tranformation-map
  [& {:as opts}]
  (let [context (get opts :context)
        nspace (get opts :nspace)]
    {:IGNORE (fn [_] "skipped")
     :METHOD identity
     :PATH identity
     :ARGS str
     :CONTEXT str
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

                  (PUT \"/path/of/uri\"
                    [a b]
                    ((keyword a) (map () [0 1 2])))

                  (context \"/manage\" [] something)
                  )"))
example

(insta/transform (r-tranformation-map) example)
