(ns routeparse.core
  (:require [clojure.string :as st]
            [instaparse.core :as insta]))

(def r-parser
  (insta/parser "default-grammar.bnf" :auto-whitespace :standard))

(r-parser "(defroutes thename
                  (GET \"/path/of/uri\"
                    [x]
                    (println x)))")

