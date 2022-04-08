(ns le-hacksheave.core
  (:require [clojure.edn :as edn]
            [hugsql.core :as hugsql]
            [le-hacksheave.scrape.lastfm :as lastfm]
            [clojure.core.match :refer [match]]
            [clojure.string :as string])
  (:gen-class))

(declare create-albums-table-if-not-exists)
(declare clear-albums)
(declare insert-albums)
(declare random-album)
(hugsql/def-db-fns "le_hacksheave/core.sql")

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "le-hacksheave.sqlite3"})

(def conf
  (-> "env.edn"
      slurp
      edn/read-string))

(defn scrape
  []
  (clear-albums db)
  (let [albums (apply lastfm/fetch-niche-albums (vals conf))
        rows (map lastfm/album->row albums)]
    (insert-albums db {:albums rows})))

(defn pick
  []
  (->> (random-album db)
       vals
       (string/join "\n")
       println))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ((match (vec args)
     ["scrape"] scrape
     ["pick"] pick)))
