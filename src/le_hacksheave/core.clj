(ns le-hacksheave.core
  (:require [clojure.edn :as edn]
            [hugsql.core :as hugsql]
            [le-hacksheave.rym :as rym])
  (:gen-class))

(declare create-albums-table-if-not-exists)
(declare insert-albums)
(declare random-album)
(hugsql/def-db-fns "le_hacksheave/core.sql")



(defn -main
  "I don't do a whole lot ... yet."
  [& _args]
  ())

(comment
  (def conf
    (-> "env.edn"
        slurp
        edn/read-string))
  (def niche-albums
    (fetch-niche-albums (:api-key conf)
                        "jpegaga"
                        (:min-playcount conf)
                        (:start-from-page conf)))
  (def to-insert (map album->row niche-albums))
  (def db
    {:classname "org.sqlite.JDBC"
     :subprotocol "sqlite"
     :subname "le-hacksheave.sqlite3"})
  (insert-albums db {:albums [["a" "b" "c"]]})
  (rym/fetch-album-genres))
