(ns routeparse.utils
  (:import (clojure.lang RT)
           (java.io PushbackReader LineNumberReader InputStreamReader)))

(defn- from-path
  [path]
  (symbol (clojure.string/replace
            (clojure.string/replace path #".clj" "")
            #"/" ".")))

(defn my-source-fn
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