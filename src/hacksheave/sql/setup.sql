CREATE TABLE IF NOT EXISTS Genre (
    name text,
    url text,
    pages_count integer, -- number of pages of albums in the RYM genre page
    id text PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Album (
    name text,
    artist text,
    cover_art_url text,
    origin text CHECK (origin IN ('lastfm', 'rym')),
    PRIMARY KEY (name, artist)
);

CREATE TABLE IF NOT EXISTS AlbumGenre (
    name text,
    artist text,
    genre_id text,
    FOREIGN KEY (genre_id) REFERENCES Genre (genre_id)
);
