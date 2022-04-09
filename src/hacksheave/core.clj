(ns hacksheave.core
  (:require [clojure.edn :as edn]
            [hugsql.core :as hugsql]
            [hacksheave.scrape.lastfm :as lastfm]
            [clojure.core.match :refer [match]]
            [clojure.string :as string])
  (:gen-class))

(declare insert-albums)
(declare random-album)
(hugsql/def-db-fns "hacksheave/sql/hug-funcs.sql")

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "hacksheave.sqlite3"})

(def conf
  (-> "env.edn"
      slurp
      edn/read-string))

(defn scrape
  []
  (let [albums (apply lastfm/fetch-niche-albums (vals conf))
        rows (map lastfm/album->row albums)]
    (insert-albums db {:albums rows})))

(defn pick
  []
  (->> (random-album db)
       vals
       (string/join "\n")
       println))

(def usage
  "Usage: hacksheave COMMAND
Available commands:

  scrape - Swift through your Last.fm page, saving niche albums into an SQLite3 file
  pick - Print a random album from the scraped ones")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (match (vec args)
    ["scrape"] (scrape)
    ["pick"] (pick)
    :else (println usage)))
