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

(def genre-title-pattern #"Diverse (.*) albums of all-time")

(defn- genre-title
  [^Document page]
  (let [h1 (r/extract page [] ".page_chart_header h1" r/text)]
    (if (.startsWith h1 "Custom chart:")
      nil
      (second (re-find genre-title-pattern h1)))))

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

(defn- get-page-count-or-fetch
  [genre-id]
  (let [{c :page_count} (db/get-genre-page-count db/spec {:id genre-id})]
    (or c
        (let [page (-> (format genre-page-url-format genre-id 1)
                       http/get
                       :body
                       r/parse)
              page-count (genre-page-count page)]
          (db/insert-genre
            db/spec
            {:name (genre-title page) :id genre-id :page_count page-count})
          page-count))))

(defn- rand-in-range
  [max-num min-percentile max-percentile]
  (let [lower-limit (* max-num (/ min-percentile 100))
        upper-limit (* max-num (/ max-percentile 100))]
    ;; pick a random number between `lower-limit` and `upper-limit`
    (-> upper-limit
        (- lower-limit)
        rand-int
        ;; throw in an extra 1 because `rand-int`'s upper limit is exclusive
        (+ lower-limit 1)
        Math/floor
        int)))

(defn pick-some-albums-from-genre
  [genre-id min-percentile max-percentile]
  (let [page-count (get-page-count-or-fetch genre-id)
        ;; picks & downloads a random page between 1 and `page-count`
        page (-> (format
                   genre-page-url-format
                   genre-id
                   (rand-in-range page-count min-percentile max-percentile))
                 http/get
                 :body
                 r/parse)]
    (albums-in-genre-page page)))
