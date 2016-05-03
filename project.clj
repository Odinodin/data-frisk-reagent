(defproject data-frisk-reagent "0.1.3"
  :description "Frisking EDN since 2016!"
  :url "http://github.com/odinodin/data-frisk-reagent"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :min-lein-version "2.6.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [reagent "0.6.0-alpha" :exclusions [cljsjs/react]]
                 [cljsjs/react-with-addons "0.14.3-0"]]
  :plugins [[lein-figwheel "0.5.2"]
            [lein-cljsbuild "1.1.3" :exclusions [[org.clojure/clojure]]]]
  :source-paths ["src"]

  :figwheel {:http-server-root "public"}

  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [figwheel-sidecar "0.5.2"]
                                  [devcards "0.2.1-6" :exclusions [[cljsjs/react]]]]
                   :source-paths ["src" "devcards"]
                   :resource-paths ["devresources"]
                   :cljsbuild {
                               :builds [{:id "dev"
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
                                                    :output-dir "resources/public/js/out-cards"}}]}}}
  :clean-targets ^{:protect false} ["resources/public/js" "target"])
