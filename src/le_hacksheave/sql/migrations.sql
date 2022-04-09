-- should just be ran manually against the DB

CREATE TABLE IF NOT EXISTS Genre (name TEXT, url TEXT, id TEXT);
CREATE TABLE IF NOT EXISTS Album (name TEXT, artist TEXT, cover_art_url TEXT);
CREATE TABLE IF NOT EXISTS AlbumGenre (name TEXT, artist TEXT, cover_art_url TEXT);
