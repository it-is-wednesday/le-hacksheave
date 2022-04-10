(ns hacksheave.db
  (:require [hugsql.core :as hugsql]))

(def spec
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "hacksheave.sqlite3"})

(declare insert-albums)
(declare random-album)
(declare get-genre-page-count)
(hugsql/def-db-fns "hacksheave/sql/hug-funcs.sql")
