-- :name clear-albums :execute :raw
-- :doc Delete all rows from Album

DELETE
FROM Album
WHERE 1;

-- :name insert-albums :insert :affected
-- :doc Insert albums to the Albums table

INSERT
OR
IGNORE INTO Album (name, artist, cover_art_url, origin)
VALUES :tuple*:albums;

-- :name random-album :query :one
-- :doc Pick a random album

SELECT *
FROM Album
ORDER BY RANDOM()
LIMIT 1;

-- :name get-genre-page-count :query :one
-- :doc Get the number of pages of albums this genre has on its RateYourMusic page

SELECT page_count
FROM Genre
WHERE id = :id;

-- :name insert-genre :execute :raw
-- :doc Insert a new genre

INSERT INTO Genre (name, id, page_count)
VALUES (:name, :id, :page_count);
