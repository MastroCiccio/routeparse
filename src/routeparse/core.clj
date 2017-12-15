(ns routeparse.core
  (:require [clojure.string :as st]
            [instaparse.core :as insta]
            [routeparse.utils :as utils]))

(def rp-parser
  (insta/parser "default-grammar.bnf" :auto-whitespace :standard))

(defn- rp-tranformation-map
  [& {:as opts}]
  (let [context (get opts :context)
        nspace (get opts :nspace)]
    {:IGNORE (fn [_] "skipped")
     :METHOD identity
     :PATH #(str context %)
     :ARGS str
     :CONTEXT (fn [path & handler]
                (let [sym (symbol nspace (-> handler first st/trim))
                      ns-path (utils/from-sym (namespace sym))]
                  (insta/transform (rp-tranformation-map :context path)
                                   (rp-parser (utils/handler-source ns-path sym)))

                  ))
     :ROUTE (fn [& args]
              (if (map? (first args))
                (first args)
                (st/join " " args)))
     :HANDLER_NAME keyword
     :HANDLER (fn [name & routes]
                {name
                 (reduce (fn [res item]
                            (if (map? item)
                              (apply into res (vals item))
                              (cons item res)))
                         () routes)})
     }))

(def srcexample (utils/handler-source "routeparse/utils.clj" 'ex-routes))
srcexample

(def example (rp-parser srcexample))
example

(insta/transform (rp-tranformation-map :nspace "routeparse.utils") example)
