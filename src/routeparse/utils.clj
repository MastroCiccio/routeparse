(ns routeparse.utils
  (:import (clojure.lang RT)
           (java.io PushbackReader LineNumberReader InputStreamReader))
  (:require [compojure.core :refer [defroutes POST GET context]]))

(defn from-path
  [path]
  (symbol (clojure.string/replace
            (clojure.string/replace path #".clj" "")
            #"/" ".")))

(defn from-sym
  [sym]
  (-> sym name (clojure.string/replace #"\." "/") (str ".clj")))

(defn handler-source
  "Custom version of source-fn in clojure.repl"
  [ns-path x]
  (when-let [v (ns-resolve (from-path ns-path) x)]
    (when-let [filepath ns-path]
      (when-let [strm (.getResourceAsStream (RT/baseLoader) filepath)]
        (with-open [rdr (LineNumberReader. (InputStreamReader. strm))]
          (dotimes [_ (dec (:line (meta v)))] (.readLine rdr))
          (let [text (StringBuilder.)
                pbr (proxy [PushbackReader] [rdr]
                      (read [] (let [i (proxy-super read)]
                                 (.append text (char i))
                                 i)))
                read-opts (if (.endsWith ^String filepath "cljc") {:read-cond :allow} {})]
            (if (= :unknown *read-eval*)
              (throw (IllegalStateException. "Unable to read source while *read-eval* is :unknown."))
              (read read-opts (PushbackReader. pbr)))
            (str text)))))))

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

  (context "/manage" [] something))