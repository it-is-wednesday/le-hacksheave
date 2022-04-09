CREATE TABLE IF NOT EXISTS Genre (
    name text,
    url text,
    id text PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Album (
    name text,
    artist text,
    cover_art_url text,
    PRIMARY KEY (name, artist)
);

CREATE TABLE IF NOT EXISTS AlbumGenre (
    name text,
    artist text,
    genre_id text,
    FOREIGN KEY (genre_id) REFERENCES Genre (genre_id)
);
