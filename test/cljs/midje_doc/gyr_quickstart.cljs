(ns midje-doc.gyr-quickstart
  (:require [purnam.test])
  (:use-macros [purnam.core :only [! f.n def.n obj arr]]
               [gyr.core :only [def.module def.controller 
                                      def.value def.constant 
                                      def.filter def.factory 
                                      def.provider def.service
                                      def.directive def.config]]
               [purnam.test :only [describe is it]]
               [gyr.test :only [describe.ng describe.controller it-uses]]))

[[:section {:title "DSL for Angular"}]]

"[Angular.js](http://angularjs.org) is the premier javascript framework for building large-scale, single-page applications. [gyr.core](#gyr-core) allows clean, readable definitions of angular.js modules and tests. Below is the definition of an angular.js module, a storage service, and a controller that uses the service."

[[{:title "module definition"}]]

(def.module myApp [])

(def.service myApp.storage []
  (let [store (atom {})]
    (obj :put (fn [k v] (swap! store #(assoc % k v)))
         :get (fn [k] (@store k))
         :clear (fn [] (reset! store {}))
         :print (fn [] (js/console.log (clj->js @store))))))

(def.controller myApp.AppCtrl [$scope storage]
  (! $scope.key "hello")
  (! $scope.val "world")  
  (! $scope.printStore storage.print) 
  (! $scope.clearStore storage.clear)
  (! $scope.putStore storage.put)
  (! $scope.getStore storage.get))

[[:section {:title "Testing for Angular"}]]
  
"Testing angular.js apps are quite brain intensive when using pure javascript. The [gyr.test](#gyr-test) namespace takes care of all the injections for us"

[[{:title "testing services"}]]
(describe.ng
  {:doc "Storage"
   :module myApp
   :inject [storage]}
  (it "allows saving and retriving"
    (storage.put "hello" "world")
    (is (storage.get "hello") "world")
    
    (storage.clear)
    (is (storage.get "hello") nil)))

[[{:title "testing controllers"}]]
(describe.controller 
  {:doc "AppCtrl"
   :module myApp
   :controller AppCtrl}
   
 (it "has key and val within the scope"
   (is $scope.key "hello")
   (is $scope.val "world"))
 
 (it "has put and get functionality"
   ($scope.putStore $scope.key $scope.val)
   (is ($scope.getStore "hello") "world"))
   
 (it "additional tests"
   (! $scope.key "bye")
   ($scope.putStore $scope.key $scope.val)
   (is ($scope.getStore "hello") nil)
   (is ($scope.getStore "bye") "world")))


