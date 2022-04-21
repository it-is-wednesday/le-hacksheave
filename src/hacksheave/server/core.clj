(ns hacksheave.server.core
  (:require [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [selmer.parser :as selmer]
            [compojure.core :refer [defroutes GET]]))

(defroutes app-routes (GET "/" [] (selmer/render-file "index.html" {})))

(def app (wrap-defaults app-routes site-defaults))
