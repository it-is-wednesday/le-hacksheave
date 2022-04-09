(ns hacksheave.scrape.rym
  "RateYourMusic scraper, scraping the results page and genres from a release
  page"
  (:require [clj-http.client :as http]
            [reaver :as r]))

(defn genre-url->id
  "Extracts an ID out of RYM genre URL, for example:
  `/genre/contemporary-randb/` -> `contemporary-randb`"
  [url]
  (second (re-find #"/genre/(.*)/" url)))

(defn- genres
  "Extract main genre(s) from given release page, along with links to the
  genres' pages"
  [page]
  (for [genre (r/extract-from page
                              ".release_pri_genres .genre" [:genre :url]
                              "a" r/text
                              "a" (r/attr :href))]
    (assoc genre :id (genre-url->id (:url genre)))))

(defn- first-result
  "Extract the first search result from the search results page. Returns a map
  of artist name, title name and RYM page of the release"
  [page]
  (-> (r/extract-from page
                      ".page_search_results table table table td" [:artist
                                                                   :title :url]
                      ".artist" r/text
                      "i a" r/text
                      "i a" (r/attr :href))
      first
      (update :url (partial str "https://rateyourmusic.com"))))

(defn- search-album
  [title artist]
  (-> (http/get "https://rateyourmusic.com/search"
                {:query-params {"searchterm" (format "%s - %s" artist title)
                                "searchtype" "l"}})
      :body
      r/parse
      first-result))

(defn fetch-album-genres
  [{:keys [title artist]}]
  (let [result (search-album title artist)]
    (println result)
    (genres (-> result
                :url
                http/get
                :body
                r/parse))))

(comment
  (def page
    (:body (http/get "https://rateyourmusic.com/search"
                     {:query-params {"searchterm" "Kali Uchis - Isolation"
                                     "searchtype" "l"}})))
  (def p (r/parse page))
  (first (r/select p ".infobox"))
  (first (r/extract-from p
                         ".page_search_results table table table td"
                           [:artist :title :url]
                         ".artist" r/text
                         "i a" r/text
                         "i a" (r/attr :href)))
  (search-album "Isolation" "Kali Uchis")
  (def isolation-page
    (-> (http/get
          "https://rateyourmusic.com/release/album/kali-uchis/isolation/")
        :body
        r/parse))
  (genres isolation-page)
  (fetch-album-genres {:title "Isolation" :artist "Kali Uchis"}))
