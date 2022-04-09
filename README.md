# Le Hacksheave

## Setup

`env.edn` needs to exist in current directory with the following content:

``` clojure
{:api-key "..." ;; https://www.last.fm/api/account/create
 :min-playcount 20
 :start-from-page 60}
```

Also, initiate the Sqlite3 DB file:
``` shell
sqlite3 hacksheave.sqlite3 < src/hacksheave/sql/setup.sql
```
