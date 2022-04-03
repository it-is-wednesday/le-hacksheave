-- :name create-albums-table-if-not-exists
-- :command :execute
-- :result :raw
-- :doc Create albums table
CREATE TABLE IF NOT EXISTS Album (name TEXT, artist TEXT, cover_art_url TEXT)

-- :name insert-albums
-- :command :insert
-- :result :affected
-- :doc Insert albums to the Albums table
INSERT INTO Album (name, artist, cover_art_url)
VALUES :tuple*:albums
