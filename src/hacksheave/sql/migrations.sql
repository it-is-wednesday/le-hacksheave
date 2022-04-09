CREATE TABLE IF NOT EXISTS Genre (
    name text,
    url text,
    id text
);

CREATE TABLE IF NOT EXISTS Album (
    name text,
    artist text,
    cover_art_url text
);

CREATE TABLE IF NOT EXISTS AlbumGenre (
    name text,
    artist text,
    cover_art_url text
);
