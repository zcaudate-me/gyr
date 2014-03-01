# gyr

[![Build Status](https://travis-ci.org/purnam/gyr.png?branch=master)](https://travis-ci.org/purnam/gyr)

A nicer angularjs interop for clojurescript

## Usage

Stable Version: 

```clojure
[im.chit/gyr "0.3.1"] 
```

Documentation:

- [API](http://purnam.github.io/gyr/)
- [Sample Application](https://github.com/purnam/example.gyr)

## Angular Simplified

As much as I became amazed at the possibilities of angularjs, I was also experiencing alot of pain when developing with javascript. Javascript is not the best language to work with and unless one is serious javascript expert (which I am far from), it was very easy to complect modules within large *angular.js* applications. Using coffeescript did not solve the problem and in fact, made things worse with its white-space indentation style. 
 
I also found that *properly architected* angular.js applications had WAY too many files for my liking - as seen in the [Year of Moo](www.yearofmoo.com/‎) projects. I wanted to use clojure syntax so that my code was smaller, more readable and easier to handle. Essentially, I believe that great angular apps should be able to be written in one file.

Gyr currently offers:

- [gyr.core](http://purnam.github.io/gyr/#gyr-core) - a simple dsl for eliminating boilerplate *angular.js*
- [gyr.test](http://purnam.github.io/gyr/#gyr-test) - testing macros for eliminating more boilerplate test code for services, controllers, directives and filters
- `gyr.filters` - undocumented library of clojure-style filters for angular.js

##### Angular JS

```clojure
;; purnam.angular

(def.module my.app [])

(def.config my.app [$routeProvider]
  (-> $routeProvider
      (.when "/" (obj :templateUrl "views/main.html"))
      (.otherwise (obj :redirectTo "/"))))

(def.controller my.app.MainCtrl [$scope $http]
  (! $scope.msg "")
  (! $scope.setMessage (fn [msg] (! $scope.msg msg)))
  (! $scope.loginQuery
     (fn [user pass]
       (let [q (obj :user user
                    :pass pass)]
         (-> $http
             (.post "/login" q)
             (.success (fn [res]
                         (if (= res "true")
                           (! $scope.loginSuccess true)
                           (! $scope.loginSuccess false))))
             (.error (fn [] (js/console.log "error!!")))))))
```

##### AngularJS Testing
```clojure
;; purnam.test.angular

(describe.controller
 {:doc "A sample controller for testing purposes"
  :module my.app
  :controller MainCtrl}

 (it "should be able to change the message within the $scope"
  (is $scope.msg "Hello") 
  (do ($scope.setMessage "World!")
      (is $scope.msg "World!"))

  (do ($scope.setMessage "Angular Rocks!")
      (is $scope.msg "Angular Rocks!"))))
```


## License

Copyright © 2014 Chris Zheng

Distributed under the The MIT License.
