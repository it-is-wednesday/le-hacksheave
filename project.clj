(defproject hacksheave "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/core.match "1.0.0"]
                 [org.xerial/sqlite-jdbc "3.36.0.3"]
                 [com.layerware/hugsql "0.5.3"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.2"] ;; json
                 [reaver "0.1.3"] ;; HTML navigation
                 ;; server
                 [compojure "1.6.1"]
                 [selmer "1.12.44"]
                 [ring/ring-defaults "0.3.2"]
                 [javax.servlet/servlet-api "2.5"]]
  :main ^:skip-aot hacksheave.core
  :target-path "target/%s"
  :plugins [[lein-ring "0.12.6"]]
  :resource-paths ["src/hacksheave/server/resources"]
  :ring {:handler hacksheave.server.core/app :nrepl {:start? true :port 20612}}
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
