(ns le-hacksheave.core
  (:require [clj-http.client :as http]
            [clojure.edn :as edn]
            [cheshire.core :as json])
  (:gen-class))

;; ummm I think we want to collect here a local database? or at least start
;; collecting it because you'll have to cross it with RYM

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

(defn fetch-niche-albums
  [api-key user min-playcount page]
  (loop [page page
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
        (recur (inc page) (concat acc-albums albums))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& _args]
  ())

(comment
  (def conf
    (-> "env.edn"
        slurp
        edn/read-string))
  (def top-albums
    (fetch-top-albums (:api-key conf) "jpegaga" (:start-from-page conf)))
  (-> top-albums
      :body
      json/parse-string
      (get "topalbums")
      (get "album"))
  (def niche-albums
    (fetch-niche-albums (:api-key conf)
                        "jpegaga"
                        (:min-playcount conf)
                        (:start-from-page conf))))
