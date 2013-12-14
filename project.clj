(defproject im.chit/purnam-angular "0.3.2-SNAPSHOT"
  :description "Use angular.js like its angular.cljs"
  :url "http://www.github.com/zcaudate/purnam-angular"
  :license {:name "The MIT License"
            :url "http://opensource.org/licencses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [im.chit/purnam "0.3.2-SNAPSHOT"]]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "0.0-2080"]
                                  [midje "1.6.0"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-cljsbuild "1.0.0"]]}}
  :source-paths ["src/cljs"]
  :test-paths ["test/clj"]
  :documentation {:files {"docs/index"
                          {:input "test/cljs/midje_doc/purnam_angular_guide.cljs"
                           :title "purnam-angular"
                           :sub-title "Clojurescript "
                           :author "Chris Zheng"
                           :email  "z@caudate.me"
                           :tracking "UA-31320512-2"}}}
  :cljsbuild
  {:builds
   [{:source-paths ["src/cljs" "test/cljs"],
     :id "js-test",
     :compiler {:pretty-print true,
                :output-to "harness/purnam-angular-unit.js",
                :optimizations :whitespace}}]})