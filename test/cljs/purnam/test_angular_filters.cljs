(ns purnam.test-angular-filters
  (:require [goog.object :as o]
            [gyr.filters]
            [purnam.test])
  (:use-macros [purnam.core :only [obj arr ! def.n]]
               [purnam.test :only [describe it is is-not]]
               [gyr.core :only [def.module]]
               [gyr.test :only [describe.controller describe.ng
                                it-uses it-compiles it-uses-filter]]))
                                           
(def.module test [gyr.filters])

(describe.ng
 {:doc  "Testing Filters"
  :module gyr.filters
  :inject [$filter]}

 (it "can test for range filters"
  (let [r (($filter "call") 5 #(+ %1 %2 %3 %4 %5) 1 2 3 4)]
    (is r 15))))