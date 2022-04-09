(defproject hacksheave "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [clj-http "3.12.3"]
                 [enlive "1.1.6"]
                 [cheshire "5.10.2"]
                 [com.layerware/hugsql "0.5.3"]
                 [org.xerial/sqlite-jdbc "3.36.0.3"]
                 [reaver "0.1.3"]
                 [org.clojure/core.match "1.0.0"]]
  :main ^:skip-aot hacksheave.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
