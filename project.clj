(defproject data-frisk-reagent "0.4.5"
  :description "Frisking EDN since 2016!"
  :url "http://github.com/odinodin/data-frisk-reagent"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :min-lein-version "2.7.1"
  :dependencies [[reagent "0.7.0"]]
  :plugins [[lein-figwheel "0.5.14"]
            [lein-doo "0.1.8"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]
  :source-paths ["src"]

  :figwheel {:http-server-root "public"
             :server-port 3999}

  :aliases {"testing" ["do" ["clean"] ["doo" "phantom" "test" "once"]]}

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0-beta2"]
                                  [org.clojure/clojurescript "1.9.946"]
                                  [doo "0.1.8"]
                                  [com.cemerick/piggieback "0.2.2"]
                                  [figwheel-sidecar "0.5.14"]
                                  [devcards "0.2.4" :exclusions [[cljsjs/react]]]]
                   :source-paths ["src" "devcards"]
                   :resource-paths ["devresources"]
                   :cljsbuild {:builds [{:id "dev"
                                         :source-paths ["src" "dev"]
                                         :figwheel {:on-jsload "datafrisk.demo/on-js-reload"}
                                         :compiler {:main "datafrisk.demo"
                                                    :asset-path "js/out"
                                                    :output-to "resources/public/js/main.js"
                                                    :output-dir "resources/public/js/out"}}
                                        {:id "cards"
                                         :source-paths ["src" "devcards"]
                                         :figwheel {:devcards true}
                                         :compiler {:main "datafrisk.cards"
                                                    :asset-path "js/out-cards"
                                                    :output-to "resources/public/js/cards.js"
                                                    :output-dir "resources/public/js/out-cards"}}
                                        {:id "test"
                                         :source-paths ["src" "test"]
                                         :compiler {:output-to "resources/public/js/compiled/test.js"
                                                    :main datafrisk.test-runner
                                                    :optimizations :none}}]}}}
  :clean-targets ^{:protect false} ["resources/public/js" "target"])
