(ns midje-doc.api.purnam-test-angular
  (:require [purnam.core]
            [purnam.types.clojure :as types]
            [midje-doc.api.purnam-angular :as test-app])
  (:use-macros [purnam.core :only [f.n def.n obj arr]]
               [purnam.angular :only [def.module def.controller def.service]]
               [purnam.test :only [init describe is it fact facts]]
               [purnam.test.angular :only [describe.ng describe.controller it-uses it-uses-filter it-compiles]]))

[[:chapter {:title "purnam.test.angular" :tag "purnam-test-angular"}]]

[[:section {:title "init" :tag "init-test-angular"}]]

"All tests require the following within the namespace declaration."

(comment
  (:require [purnam.core])
  (:use-macros [purnam.test :only [init]]
               [purnam.test.angular :only 
                 [describe.ng it-uses it-compiles it-uses-filter]]))

[[:section {:title "services" :tag "services"}]]
"Angular constants, values, services, factories and providers are all tested the same way: with `describe.ng` and `it-uses`. There are a couple ways of testing out functionality. The easiest is with `:inject`. The definition are from previously defined examples - [constant](#def-constant), [value](#def-value)"

(describe.ng
  {:doc "The Meaning of Life"
   :module my.app
   :inject [MeaningOfLife AnotherMeaningOfLife]}
   
  (it "should be contentious"
    (is MeaningOfLife 42)
    (is AnotherMeaningOfLife "A Mystery")))

"`:inject` can also be used to bind other values within the modules"

(describe.ng
  {:doc "The Meaning of Life"
   :module my.app
   :inject [[MeaningOfLife ([] "Don't Know")]]}

  (it "should be unclear"
    (is MeaningOfLife "Don't Know")))

"or to reference other defined services:"

(describe.ng
  {:doc "The Meaning of Life"
   :module my.app
   :inject [[AnotherMeaningOfLife 
            ([MeaningOfLife] 
              (+ 13 MeaningOfLife))]]}

  (it "should be another random number"
    (is AnotherMeaningOfLife 55)))

"[services](#def-service) are tested the same way. However this time, we will use `it-uses` to inject them into the test suite. It uses just puts the default"

(describe.ng
  {:doc "Login Service"
   :module my.app}
  
  (it-uses [LoginService]
    (is LoginService.user.login "login")
    (is LoginService.user.password "secret")))

[[:section {:title "filters" :tag "filters"}]]

"Filters are dependent on the `$filters` service and so are tested in the following way."

(describe.ng
 {:doc  "Testing Filters"
  :module my.app}

 (it-uses [$filter]
  (let [r (($filter "range") (arr) 5)]
    (is r.length 5)
    (is r (arr 0 1 2 3 4)))))
    
"A convience macro `it-uses-filter` can also be used to achieve the same effect."

(describe.ng
 {:doc  "Testing Filters"
  :module my.app}

  (it-uses-filter [range]
    (let [r (range (arr) 5)]
     (is r.length 5)
     (is r (arr 0 1 2 3 4)))))

[[:section {:title "directives" :tag "directives"}]]

"Directives require the `$compile` and `$rootScope` services."

(describe.ng
 {:doc  "Directives"
  :module my.app
  :inject [$compile $rootScope]}

 (it "should change the html output"
     (let [ele (($compile "<div app-welcome>User</div>")
                $rootScope)]
                ;;(js/console.log ele (ele.html))
       (is (ele.html) (type "Welome <strong>User</strong>")))))

"For convenience, there is the `it-compiles` macro which hides the implementation details."

(describe.ng
 {:doc  "Directives"
  :module my.app}

 (it-compiles [ele "<div app-welcome>User</div>"]
  (is (ele.html) (type "Welome <strong>User</strong>"))))

[[:section {:title "controllers" :tag "controllers"}]]

"Controllers require a bit more boilerplate to set up testing. The following is a test for [SimpleCtrl](#def-controller) using describe.ng"

(describe.ng
 {:doc "A sample controller for testing purposes"
  :module my.app
  :inject [[$scope ([$rootScope $controller]
                     ($controller "SimpleCtrl" (obj :$scope ($rootScope.$new))))]]}
                     
 (it "should set a message within the $scope"
   (is $scope.msg "Hello"))

 (it "should be able to change the message within the $scope"
   (do ($scope.setMessage "World!")
       (is $scope.msg "World!"))

   (do ($scope.setMessage "Angular Rocks!")
       (is $scope.msg "Angular Rocks!"))))

"`describe.controller` hides all implementation details to make the tests much more clearer to read."

(describe.controller
 {:doc "A sample controller for testing purposes"
  :module my.app
  :controller SimpleCtrl}

 (it "should set a message within the $scope"
   (is $scope.msg "Hello"))

 (it "should be able to change the message within the $scope"
   (do ($scope.setMessage "World!")
       (is $scope.msg "World!"))

   (do ($scope.setMessage "Angular Rocks!")
       (is $scope.msg "Angular Rocks!"))))


