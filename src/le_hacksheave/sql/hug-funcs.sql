-- :name clear-albums
-- :command execute
-- :result :raw
-- :doc Delete all rows from Album
DELETE FROM Album WHERE 1

-- :name insert-albums
-- :command :insert
-- :result :affected
-- :doc Insert albums to the Albums table
INSERT INTO Album (name, artist, cover_art_url)
VALUES :tuple*:albums

-- :name random-album
-- :command :query
-- :result :one
-- :doc Pick a random album
SELECT *
  FROM Album
 ORDER BY RANDOM()
 LIMIT 1
