(ns purnam.test-angular
  (:use [purnam.native :only [aget-in aset-in]])
  (:require [goog.object :as o])
  (:require-macros [purnam.core :as j])
  (:use-macros [purnam.core :only [obj arr ! def.n]]
               [purnam.test :only [init describe it is is-not]]
               [purnam.angular :only [def.module def.config def.factory
                                      def.filter def.controller
                                      def.service def.directive]]
               [purnam.test.angular :only [describe.controller describe.ng
                                           it-uses it-compiles it-uses-filter]]))


(def.module sample.filters [])

(def.filter sample.filters.range []
  (fn [input total]
    (when input
      (doseq [i (range (js/parseInt total))]
        (input.push i))
      input)))

(let [spec (js-obj)]
  (js/describe "Testing Filters"
               (clojure.core/fn [] (js/beforeEach
                                    (js/module "sample.filters"))
                 (js/beforeEach
                  (js/inject
                   (array "$filter"
                          (fn [$filter]
                            (aset spec "$filter" $filter)))))

                 (it (let [r ((let [obj# (purnam.native/aget-in spec [])
                                    fn# (aget obj# "$filter")]
                                (.call fn# obj# "range")) (arr) 5)]
                       (is (purnam.native/aget-in r ["length"]) 5)
                       (is (purnam.native/aget-in r ["0"]) 0))) nil)))


(describe.ng
 {:doc  "Testing Filters"
  :module sample.filters
  :inject [$filter]}

 (it "can test for range filters"
  (let [r (($filter "range") (arr) 5)]
    (is r.length 5)
    (is r.0 0)
    (is r.1 1))))

(describe.ng
 {:doc  "Testing Filters"
  :module sample.filters}

 (it-uses [$filter]
  (let [r (($filter "range") (arr) 5)]
    (is r.length 5)
    (is r.0 0)))

 (it-uses-filter
  [range]
  (is-not range nil)
  (let [r (range (arr) 5)]
    (is r.length 5)
    (is r.0 0))))

(def.module sample [sample.filters])

(def.directive sample.spWelcome []
  (fn [$scope element attrs]
    (let [html (element.html)]
      (element.html (str "Welome <strong>" html "</strong>")))))

(describe.ng
 {:doc  "Testing Directives"
  :module sample
  :inject [$compile $rootScope]}

 (it "Testing the Compilation"
     (let [ele (($compile "<div sp-welcome>User</div>")
                $rootScope)]
       (is (ele.html) "Welome <strong>User</strong>"))))

(describe.ng
 {:doc  "Testing Directives"
  :module sample}

 (it-compiles [ele "<div sp-welcome>User</div>"]
  (is (ele.html) "Welome <strong>User</strong>")))


(def.service sample.SimpleService []
  (obj :user {:login "login"}
       :changeLogin (fn [login]
                      (! this.user.login login))))

 ;;
 ;; Angular Module Testing for Simple Service
 ;;

(describe.ng
 {:doc  "Simple Services Test"
  :module sample
  :inject [SimpleService]
  :globals [compare (obj :login "login"
                         :password "secret"
                         :greeting "hello world")]}

 (it "SimpleService Basics"
     (is-not SimpleService.user compare)
     ;;(is-equal SimpleService.user compare)
     )

 (it "SimpleService Change Login"
     (is SimpleService.user.login "login")

     (do (SimpleService.changeLogin "newLogin")
         (is SimpleService.user.login "newLogin")))

 (it "SimpleService Change Login"
     (is SimpleService.user.login "login")))

 ;;
 ;; Angular Test Controller Example
 ;;


(def.controller sample.SimpleCtrl [$scope]
  (! $scope.msg "Hello")
  (! $scope.setMessage (fn [msg] (! $scope.msg msg))))

 ;;
 ;; Controller Testing
 ;;


(describe.ng
 {:doc "A sample controller for testing purposes"
  :module sample
  :inject [[$scope ([$rootScope $controller]
                     ($controller "SimpleCtrl" (obj :$scope ($rootScope.$new))))]]}
                     
 (it "should have an object called `spec`"
       (is-not spec js/undefined))

 (it "should set a message within the $scope"
       (is spec.$scope.msg "Hello")  ;; The $scope is automatically registered for us
       (is $scope.msg "Hello")      ;; We can also use spec.$scope
       )

 (it "should be able to change the message within the $scope"
       (do ($scope.setMessage "World!")
           (is $scope.msg "World!"))

       (do ($scope.setMessage "Angular Rocks!")
           (is $scope.msg "Angular Rocks!"))))

(describe.controller
 {:doc "A sample controller for testing purposes"
  :module sample
  :controller SimpleCtrl}

 (it "should have an object called `spec`"
       (is-not spec js/undefined))

 (it "should set a message within the $scope"
       (is spec.$scope.msg "Hello")  ;; The $scope is automatically registered for us
       (is $scope.msg "Hello")      ;; We can also use spec.$scope
       )

 (it "should be able to change the message within the $scope"
       (do ($scope.setMessage "World!")
           (is $scope.msg "World!"))

       (do ($scope.setMessage "Angular Rocks!")
           (is $scope.msg "Angular Rocks!"))))

(def.module hello [])
(def.factory hello.Data [] 
 (obj :a 1))

(describe.ng
{:doc  "Simple Services Test"
 :module hello
 :inject [Data]}
(it "SimpleService Change Login"
  (is Data.a 1)
  (is 1 1)
  ))
