(ns midje-doc.api.purnam-angular
  (:require [purnam.core])
  (:use-macros [purnam.core :only [! f.n def.n obj arr]]
               [purnam.angular :only [def.module def.controller 
                                      def.value def.constant 
                                      def.filter def.factory 
                                      def.provider def.service
                                      def.directive def.config]]
               [purnam.test :only [describe is it fact facts]]
               [purnam.test.angular :only [describe.ng describe.controller it-uses]]))

[[:chapter {:title "purnam.angular" :tag "purnam-angular"}]]

"Libraries to work with angular.js"

[[:section {:title "init" :tag "init-angular"}]]

"There is a dependency on [purnam.native](#purnam-cljs) and so the following MUST be placed in your project namespace:"

(comment
  (:use [purnam.native :only [aget-in aset-in]]))

[[:section {:title "def.module" :tag "def-module"}]]

"`def.module` provides an easy way to define angular modules. The following clojurescript code generates the equivalent javascript code below it:"

[[{:hide true}]]
(def.module my.app [])

(comment
  (def.module my.app [ui ui.bootstrap]))

[[{:lang "js"}]]
[[:code "angular.module('my.app', ['ui', 'ui.bootstrap'])"]]

"Typically, the `def.module` is at the very top of the file, one module is defined for one clojure namespace."

[[:section {:title "def.config" :tag "def-config"}]]

"`def.config` is used to setup module providers. "

(comment
  (def.config <MODULE NAME> [... <PROVIDERS> ...]
     ... 
     <FUNCTION BODY>
     ...     ))
     
"It is most commonly used to setup the routing for an application."

(comment
  (def.config my.app [$locationProvider $routeProvider]
    (doto $locationProvider (.hashPrefix "!"))
    (doto $routeProvider
      (.when "" (obj :redirectTo "/home")))))

"The equivalent javascript code can be seen below."

[[{:lang "js"}]]
[[:code "angular.module('my.app')
         .config(['$locationProvider', '$routeProvider', 
               function($locationProvider, $routeProvider){
                 $locationProvider.hashPrefix('!');
                 $routeProvider.when('', {redirectTo: '/home'});
           }]);"]]
           
[[:section {:title "def.controller" :tag "def-controller"}]]

"`def.controller` defines a controller. The typical usage is like this:"

(comment
  (def.controller <MODULE NAME>.<CONTROLLER NAME> [... <INJECTIONS> ...]
     ... 
     <CONTROLLER BODY>
     ... ))

"A sample controller"
   
(def.controller my.app.SimpleCtrl [$scope]
   (! $scope.msg "Hello")
   (! $scope.setMessage (fn [msg] (! $scope.msg msg))))

"Produces the equivalent javascript code:"

[[{:lang "js"}]]     
[[:code "angular.module('my.app')
          .controller('SimpleCtrl', ['$scope', function($scope){
                    $scope.msg = 'Hello'
                    $scope.setMessage = function (msg){
                      $scope.msg = msg;
                    }}])"]]

[[:section {:title "def.directive" :tag "def-directive"}]]

"`def.directive` defines a directive. The typical usage is like this:"

(comment
  (def.directive <MODULE NAME>.<DIRECTIVE NAME> [... <INJECTIONS> ...] 
     ;; Initialisation code to return a function:
     (fn [$scope element attrs]
        .... <FUNCTION> ....  ))
)
  
"A sample directive"

(def.directive my.app.appWelcome []
  (fn [$scope element attrs]
    (let [html (element.html)]
      (element.html (str "Welcome <strong>" html "</strong>")))))

"Produces the equivalent javascript code:"

[[{:lang "js"}]]
[[:code "angular.module('my.app')
        .directive('appWelcome', [function() {
          return function($scope, element, attrs) {
             var html = element.html();
             element.html('Welcome: <strong>' + html + '</strong>');
          };}]);"]]

[[:section {:title "def.filter" :tag "def-filter"}]]

"`def.filter` defines a filter. The typical usage is like this:"

(comment
  (def.filter <MODULE NAME>.<FILTER NAME> [... <INJECTIONS> ...]
   
     ;; Initialisation code to return a function:
   
     (fn [input & args]
        .... <FUNCTION> .... )))

"The sample filter"

(def.filter my.app.range []
  (fn [input total]
    (when input
      (doseq [i (range (js/parseInt total))]
        (input.push i))
      input)))

"Produces the equivalent javascript code:"

[[{:lang "js"}]]
[[:code "angular.module('my.app')
      .filter('range', [function() {
        return function(input, total) {
          if(!input) return null;
          total = parseInt(total);
          for (var i=0; i <total; i++)
            input.push(i);
          return input;
        };
      }]);"]]

[[:section {:title "def.constant" :tag "def-constant"}]]

"`def.constant` defines a constant. The typical usage is like this:"

(comment
  (def.value <MODULE NAME>.<CONSTANT NAME>
    <CONSTANT>))
  
"The sample constant"
    
(def.constant my.app.MeaningOfLife 42)

"Produces the equivalent javascript code:"

[[{:lang "js"}]]  
[[:code 
  "angular.module('my.app')
       .constant('MeaningOfLife', 42);"]]

[[:section {:title "def.value" :tag "def-value"}]]

"`def.value` defines a value. The typical usage is like this:"

(comment
  (def.value <MODULE NAME>.<VALUE NAME>
    <VALUE>))
  
"The sample value"
    
(def.value my.app.AnotherMeaningOfLife "A Mystery")

"Produces the equivalent javascript code:"
  
[[{:lang "js"}]]  
[[:code 
  "angular.module('my.app')
       .value('AnotherMeaningOfLife', 'A Mystery');"]]

[[:section {:title "def.service" :tag "def-service"}]]

"`def.service` defines a service. The typical usage is like this:"

(comment
  (def.service <MODULE NAME>.<SERVICE NAME> [... <INJECTIONS> ...]
      <RETURN OBJECT>  ))

"The sample service"

(def.service my.app.LoginService []
  (obj :user {:login "login"
              :password "secret"
              :greeting "hello world"}
       :changeLogin (fn [login]
                      (! this.user.login login))))

"Produces the equivalent javascript code:"

[[{:lang "js"}]]
[[:code
"angular.module('my.app')
       .service('LoginService', [function(){
         return {user: {:login 'login',
                        :password 'secret',
                        :greeting 'hello world'},
                 changeLogin: function (login){
                                  this.user.login = login;}}}]);"]]
                                  
[[:section {:title "def.factory" :tag "def-factory"}]]

"`def.factory` defines a factory. The typical usage is like this:"

(comment
  (def.factory <MODULE NAME>.<FACTORY NAME> [... <INJECTIONS> ...]
      <RETURN OBJECT>  ))

"The sample factory"
      
(comment
  (def.factory my.app.LoginService []
    (obj :user {:login "login"
                :password "secret"
                :greeting "hello world"}
         :changeLogin (fn [login]
                        (! this.user.login login)))))

"Produces the equivalent javascript code:"

[[{:lang "js"}]]
[[:code
"angular.module('my.app')
       .factory('LoginService', [function(){
         return {user: {:login 'login',
                        :password 'secret',
                        :greeting 'hello world'},
                 changeLogin: function (login){
                                  this.user.login = login;}}}]);"]]


[[:section {:title "def.provider" :tag "def-provider"}]]

"`def.provider` defines a provider. The typical usage is like this:"

(comment
  (def.provider <MODULE NAME>.<SERVICE NAME> [... <INJECTIONS> ...]
      <RETURN OBJECT>  ))
      
"The following is a definition, configuration, and usage of a provider"

(def.provider my.app.HelloWorld []
  (obj :name "Default"
       :$get (fn []
               (let [n self.name]
                 (obj :sayHello
                      (fn [] (str "Hello " n "!")))))
       :setName (fn [name]
                  (! self.name name))))

(def.config my.app [HelloWorldProvider]
  (HelloWorldProvider.setName "World"))

(def.controller my.app.sfpMainCtrl [$scope HelloWorld]
  (! $scope.hello (str (HelloWorld.sayHello) " From Provider")))
