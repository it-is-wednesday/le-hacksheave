(ns hacksheave.core
  (:require [clojure.edn :as edn]
            [clojure.core.match :refer [match]]
            [clojure.string :as string]
            [hacksheave.db :as db]
            [hacksheave.scrape.lastfm :as lastfm])
  (:gen-class))

(def conf
  (-> "env.edn"
      slurp
      edn/read-string))

(defn scrape
  []
  (let [albums (lastfm/fetch-niche-albums (:lastfm conf))
        rows (map lastfm/album->row albums)]
    (db/insert-albums db/spec {:albums rows})))

(defn pick
  []
  (->> (db/random-album db/spec)
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
