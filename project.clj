(defproject data-drill-reagent "0.1.0-SNAPSHOT"
  :description "Drilling EDN since 2016!"
  :url "http://github.com/odinodin/data-drill-reagent"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :min-lein-version "2.6.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [reagent "0.6.0-alpha"]]
  :plugins [[lein-figwheel "0.5.2"]
            [lein-cljsbuild "1.1.3" :exclusions [[org.clojure/clojure]]]]
  :source-paths ["src"]

  :figwheel {:http-server-root "public"}

  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [figwheel-sidecar "0.5.2"]]
                   :source-paths ["src" "dev"]
                   :cljsbuild {
                               :builds [{:id "dev"
                                         :source-paths ["src/"]
                                         :figwheel {:on-jsload "datadrill.core/on-js-reload"}
                                         :compiler {:main "datadrill.core"
                                                    :asset-path "js/out"
                                                    :output-to "resources/public/js/main.js"
                                                    :output-dir "resources/public/js/out"}}]}}}
  :clean-targets ^{:protect false} ["resources/public/js" "target"])
