(ns hacksheave.scrape.rym
  "RateYourMusic scraper, scraping the results page and genres from a release
  page"
  (:import (org.jsoup.nodes Document))
  (:require [hacksheave.db :as db]
            [clj-http.client :as http]
            [reaver :as r]))

(def genre-page-url-format
  "There's one format parameter: the genre id"
  "https://rateyourmusic.com/charts/diverse/album/all-time/g:%s/exc:live,archival/%d")

(defn- genre-url->id
  "Extracts an ID out of RYM genre URL, for example:
  `/genre/contemporary-randb/` -> `contemporary-randb`"
  [url]
  (second (re-find #"/genre/(.*)/" url)))

(defn- genre-page-count
  "Return the number of pages of albums a genre has, given any page out of the
  genre pages."
  [^Document page]
  (-> page
      (r/extract-from "#ui_pagination_pages_page_chart_top" []
                      ".ui_pagination_number" r/text)
      first
      last
      Integer/parseInt))

(defn- albums-in-genre-page
  [page]
  (r/extract-from page
                  ".chart_results .topcharts_textbox_top" [:title :artist]
                  ".release" r/text
                  ".artist" r/text))

(defn- fetch-page-count
  [genre-id]
  (-> (format genre-page-url-format genre-id 1)
      http/get
      :body
      r/parse
      genre-page-count))

(defn- get-page-count-or-fetch
  [genre-id]
  (let [{c :page_count} (db/get-genre-page-count db/spec {:id genre-id})]
    (or c (fetch-page-count genre-id))))

(defn pick-some-albums-from-genre
  [genre-id]
  (let [page-count (get-page-count-or-fetch genre-id)
        ;; picks & downloads a random page between 1 and `page-count`
        page (-> (format genre-page-url-format
                         genre-id
                         (inc (rand-int page-count)))
                 http/get
                 :body
                 r/parse)]
    (albums-in-genre-page page)))
