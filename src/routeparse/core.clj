(ns routeparse.core
  (:require [clojure.string :as st]
            [instaparse.core :as insta]
            [routeparse.utils :as utils]
            [routeparse.routes :as routes]))

(def rp-parser
  (insta/parser "default-grammar.bnf" :auto-whitespace :standard))

(defn recur-path
  [path]
  (let [pre (st/replace path #"^routeparse/" "")
        patt (re-find #"[^/]*routeparse/.+" pre)]
    (if (nil? patt)
      path
      (recur-path patt))))
;;(recur-path "/home/francesco/Documenti/Projects/Clojure/routeparse/src/routeparse/exports.clj")

(defn- rp-transformation-map
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
                  (insta/transform (rp-transformation-map :context path)
                                   (rp-parser (utils/handler-source ns-path sym)))

                  ))
     :IMPORT (fn [& args]
               (let [sym (symbol (-> args last st/trim))
                     ns-res (:file (meta (ns-resolve 'routeparse.exports sym)))
                     ns-path (recur-path ns-res)]
                 (println sym ns-path)
                 (insta/transform (rp-transformation-map)
                                  (rp-parser (utils/handler-source ns-path sym)))))
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

(def srcexample (utils/handler-source "routeparse/routes.clj" 'ex-routes))
srcexample

(def example (rp-parser srcexample))
example

(insta/transform (rp-transformation-map :nspace "routeparse.routes") example)
