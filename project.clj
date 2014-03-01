(defproject im.chit/gyr "0.3.1"
  :description "Write angular.js like its angular.cljs"
  :url "http://www.github.com/purnam/gyr"
  :license {:name "The MIT License"
            :url "http://opensource.org/licencses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [im.chit/purnam "0.4.3"]]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "0.0-2080"]
                                  [midje "1.6.0"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-cljsbuild "1.0.0"]]}}
  :test-paths ["test/clj"]
  :documentation {:files {"doc/index"
                          {:input "test/cljs/midje_doc/gyr_guide.clj"
                           :title "gyr"
                           :sub-title "Angularjs extensions for clojurescript"
                           :author "Chris Zheng"
                           :email  "z@caudate.me"
                           :tracking "UA-31320512-2"}}}
  :cljsbuild
  {:builds
   [{:source-paths ["src" "test/cljs"],
     :id "js-test",
     :compiler {:pretty-print true,
                :output-to "target/gyr-test.js",
                :optimizations :whitespace}}]})