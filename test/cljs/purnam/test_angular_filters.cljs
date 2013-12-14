(ns purnam.test-angular-filters
  (:use [purnam.native :only [aget-in aset-in]])
  (:require [goog.object :as o]
            [purnam.angular.filters :as f])
  (:require-macros [purnam.core :as j])
  (:use-macros [purnam.core :only [obj arr ! def.n]]
               [purnam.test :only [init describe it is is-not]]
               [purnam.angular :only [def.module def.config def.factory
                                      def.filter def.controller
                                      def.service def.directive]]
               [purnam.test.angular :only [describe.controller describe.ng
                                           it-uses it-compiles it-uses-filter]]))
                                           
(def.module test [purnam.filters])

(describe.ng
 {:doc  "Testing Filters"
  :module purnam.filters
  :inject [$filter]}

 (it "can test for range filters"
  (let [r (($filter "call") 5 #(+ %1 %2 %3 %4 %5) 1 2 3 4)]
    (is r 15))))