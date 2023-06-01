# Le Hacksheave

## Setup

`env.edn` needs to exist in current directory with the following content:

``` clojure
{:lastfm
 {:api-key "..." ;; https://www.last.fm/api/account/create
  :user "jpegaga"
  :min-playcount 5
  :start-from-page 20}}
```

Also, initiate the Sqlite3 DB file:
``` shell
sqlite3 hacksheave.sqlite3 < src/hacksheave/sql/setup.sql
```

## Usage
Populate the database:
``` shell
lein run scrape
```

Now, to pick a random album:
``` shell
lein run pick
```
