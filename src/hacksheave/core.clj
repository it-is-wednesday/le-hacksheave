(ns hacksheave.core
  (:require [clojure.edn :as edn]
            [clojure.core.match :refer [match]]
            [clojure.string :as string]
            [hacksheave.db :as db]
            [hacksheave.scrape.lastfm :as lastfm]
            [hacksheave.scrape.rym :as rym])
  (:gen-class))

(defn album->row
  [{:keys [title artist cover origin]}]
  [title artist cover origin])

(def conf
  (-> "env.edn"
      slurp
      edn/read-string))

(defn scrape
  []
  (db/wipe-albums db/spec)
  (let [lastfm-albums (lastfm/fetch-niche-albums (:lastfm conf))
        rym-albums (rym/fetch-niche-albums (:rateyourmusic conf))
        all-albums (concat rym-albums lastfm-albums)]
    (db/insert-albums db/spec {:albums (map album->row all-albums)})))

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

(comment
  (def lastfm-albums (lastfm/fetch-niche-albums (:lastfm conf)))
  (def rym-albums (rym/fetch-niche-albums (:rateyourmusic conf)))
  (->> (concat rym-albums lastfm-albums)
       (map :origin)
       set)
  ()
  (db/insert-albums db/spec
                    {:albums (map album->row
                               (concat rym-albums lastfm-albums))}))
