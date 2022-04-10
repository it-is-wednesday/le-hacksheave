setup-db:
    /bin/env rm hacksheave.sqlite3
    sqlite3 hacksheave.sqlite3 < src/hacksheave/sql/setup.sql
