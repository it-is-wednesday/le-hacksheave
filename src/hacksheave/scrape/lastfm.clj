(ns hacksheave.scrape.lastfm
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def lastfm-api-url "http://ws.audioscrobbler.com/2.0/")

(defn fetch-top-albums
  [api-key user page]
  (http/get lastfm-api-url
            {:query-params {"method" "user.gettopalbums"
                            "user" user
                            "api_key" api-key
                            "format" "json"
                            "period" "overall"
                            "page" page}}))

(defn- album->row
  "Extract the name, artist, and cover art from an album fetched via Last.fm's API.
  These are the details we want to save into our database"
  [album]
  {:title (get album "name")
   :artist (-> album
               (get "artist")
               (get "name"))
   :cover_art_url
     ;; `last` picks the highest resultion picture
     (-> album
         (get "image")
         (last)
         (get "#text"))
   :origin "lastfm"})

(defn fetch-niche-albums
  [{:keys [api-key user min-playcount start-from-page]}]
  (loop [page start-from-page
         acc-albums []]
    (let [albums (-> (fetch-top-albums api-key user page)
                     :body
                     json/parse-string
                     (get "topalbums")
                     (get "album"))]
      (if (< (-> albums
                 last
                 (get "playcount")
                 Integer/parseInt)
             min-playcount)
        acc-albums
        (recur (inc page) (concat acc-albums (map album->row albums)))))))

(comment
  (require '[clojure.edn :as edn])
  (def conf
    (-> "env.edn"
        slurp
        edn/read-string
        :lastfm))
  (def niche-albums (fetch-niche-albums conf))
  (def to-insert (map album->row niche-albums)))
