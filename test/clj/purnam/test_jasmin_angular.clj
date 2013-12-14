(ns purnam.test-jasmin-angular
  (:use [midje.sweet :exclude [contains]]
        purnam.checks
        purnam.test.angular))


(fact "describe.ng"
  (macroexpand-1
   '(describe.ng
     {:doc "<DESC>"
      :module <MODULE-NAME>
      :inject [[<P1> <P1-FORM>]
               <P2>
               <P3>]
      :vars [<B1> <V1>]}
     <BODY>))
  => '(let [spec (js-obj)]
        (aset spec "<B1>" <V1>)
        (js/describe
         "<DESC>"
         (clojure.core/fn []
           (js/beforeEach (js/module "<MODULE-NAME>"))
           (js/beforeEach
            (js/inject (array "<P1-FORM>" "<P3>" "<P2>"
                              (fn [<P1-FORM> <P3> <P2>]
                                (aset spec "<P1>" <P1-FORM>)
                                (aset spec "<P2>" <P2>)
                                (aset spec "<P3>" <P3>)))))
           <BODY>
           nil))))

(fact "describe.ng"
  (macroexpand-1
   '(describe.ng
     {:doc "Testing"
      :module my.app
      :inject [$http
               [$compile $compile]
               [$filter  $mockFilter]
               [$scope
                ([$rootScope]
                   ($rootScope.$new))]
               [$httpBackend
                ([$httpBackend]
                   (do (-> $httpBackend
                           (.when "GET" "/hello")
                           (.respond 200 "hello world"))
                       $httpBackend))]]
      :vars [o (range 20)]}))
  => '(let [spec (js-obj)]
        (aset spec "o" (range 20))
        (js/describe
         "Testing"
         (clojure.core/fn []
           (js/beforeEach (js/module "my.app"))
           (js/beforeEach
            (js/inject
             (array "$httpBackend" "$http" "$mockFilter" "$compile" "$rootScope"
                    (fn [$httpBackend $http $mockFilter $compile $rootScope]
                      (aset spec "$http" $http)
                      (aset spec "$compile" $compile)
                      (aset spec "$filter" $mockFilter)
                      (aset spec "$scope"
                            (let [obj# (purnam.native/aget-in $rootScope [])
                                  fn# (aget obj# "$new")] (.call fn# obj#)))
                      (aset spec "$httpBackend"
                            (do (-> $httpBackend (.when "GET" "/hello")
                                    (.respond 200 "hello world")) $httpBackend)))))) nil))))


(fact "is-uses"
  (macroexpand-1
   '(it-uses
     [$compile]
     ($compile "oeuoeuoe")))
  => '(js/it "" (clojure.core/fn []
                  (js/inject (array "$compile"
                                    (fn [$compile]
                                      ($compile "oeuoeuoe")))))))

(fact "is-uses-filter"
  (macroexpand-1
   '(it-uses-filter
     [range]
     (is-not range nil)
     (let [r (range (arr) 5)]
       (is r.length 5)
       (is r.0 0))))
  => '(js/it "" (clojure.core/fn []
                  (js/inject (array "$filter"
                                    (fn [$filter]
                                      (let [range ($filter "range")]
                                        (is-not range nil)
                                        (let [r (range (arr) 5)]
                                          (is r.length 5)
                                          (is r.0 0)))))))))

(fact "it-compiles"
  (macroexpand-1
   '(it-compiles
     [ele "<div sp-welcome>User</div>"]
     "Testing the Compilation"
     (is (ele.html) "Welome <strong>User</strong>")))
  => '(js/it "Testing the Compilation"
         (clojure.core/fn []
           (js/inject (array "$compile" "$rootScope"
                             (fn [$compile $rootScope]
                               (let [ele (($compile "<div sp-welcome>User</div>")
                                          $rootScope)]
                                 (is (ele.html) "Welome <strong>User</strong>"))))))))


(fact "describe.ng"
  (macroexpand-1
   '(describe.ng
     {:doc  "Testing Filters"
      :module sample.filters
      :inject [$filter]}

     (it (let [r (($filter "range") (arr) 5)]
           (is r.length 5)
           (is r.0 0)))))

  => '(let [spec (js-obj)]
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

(fact "describe.controller"
  (macroexpand-1
   '(describe.controller
     {:doc "<DESC>"
      :module <MODULE-NAME>
      :controller <CONTROLLER-NAME>
      :inject {:<V1> <V1-FORM>
               :<V2> <V2-FORM>}}
     (<FUNC> $scope.<VAR>)
     (<FUNC> <V1>.<VAR>)
     (<FUNC> <V2>.<VAR>)))
  =>
  '(let [spec (js-obj)]
     (js/describe
      "<DESC>"
      (clojure.core/fn []
        (js/beforeEach (js/module "<MODULE-NAME>"))
        (js/beforeEach
         (js/inject
          (array "$controller" "<V2-FORM>" "$rootScope" "<V1-FORM>"
                 (fn [$controller <V2-FORM>
                     $rootScope <V1-FORM>]
                   (aset spec "$scope"
                         (let [scp#
                               (let [obj# (purnam.native/aget-in $rootScope [])
                                     fn# (aget obj# "$new")]
                                 (.call fn# obj#))]
                           ($controller "<CONTROLLER-NAME>"
                                        (obj :$scope scp#)) scp#))
                   (aset spec ":<V1>" <V1-FORM>)
                   (aset spec ":<V2>" <V2-FORM>)))))
        (<FUNC> (purnam.native/aget-in spec ["$scope" "<VAR>"]))
        (<FUNC> (purnam.native/aget-in <V1> ["<VAR>"]))
        (<FUNC> (purnam.native/aget-in <V2> ["<VAR>"])) nil))))


  (comment
    (fact "ng"
      (macroexpand-1
       '(ng [<SERVICE>]
            "<DESC>"
            <BODY>))
      => '(js/it "<DESC>" (js/inject (array "<SERVICE>" (fn [<SERVICE>] <BODY>))))

      (macroexpand-1
       '(ng [<SERVICE>]
            <BODY>))
      => '(js/it "" (js/inject (array "<SERVICE>" (fn [<SERVICE>] <BODY>))))

      (macroexpand-1
       '(ng [<SERVICE>]
            "<DESC>"
            <BODY>)))

    (fact "ng-filter"
      (macroexpand-1
       '(ng-filter [<FILTER>]
                   "<DESC>"
                   <BODY>))
      => '(js/it "<DESC>"
                 (js/inject
                  (array "$filter"
                         (fn [$filter]
                           (let [<FILTER> ($filter "<FILTER>")]
                             <BODY>))))))

    (fact "ng-compile"
      (macroexpand-1
       '(ng-compile [<NAME> <HTML>]
                    "<DESC>"
                    <BODY>))
      => '(js/it "<DESC>"
                 (js/inject
                  (array "$compile" "$rootScope"
                         (fn [$compile $rootScope]
                           (let [<NAME> (($compile <HTML>) $rootScope)]
                             <BODY>))))))

    (fact "describe.controller"
      (macroexpand-1
       '(describe.controller
         {:doc "<DESC>"
          :module <MODULE-NAME>
          :controller <CONTROLLER-NAME>}
         <BODY>))
      =>
      '(let [spec (js-obj)]
         (describe {:doc "<DESC>"
                    :module <MODULE-NAME>
                    :controller <CONTROLLER-NAME>}
                   (js/beforeEach (js/module "<MODULE-NAME>"))
                   (js/beforeEach
                    (js/inject
                     (array "$rootScope" "$controller"
                            (fn [$rootScope $controller]
                              (! spec.$scope ($rootScope.$new))
                              ($controller "<CONTROLLER-NAME>" spec)))))
                   <BODY>)))


    (fact "describe.controller"
      (macroexpand-1
       '(describe.controller
         {:doc "<DESC>"
          :module <MODULE-NAME>
          :controller <CONTROLLER-NAME>}
         (<FUNC> $scope.<VAR>)
         (<FUNC> $ctrl.<VAR>)))
      =>
      '(let [spec (js-obj)]
         (describe
          {:doc "<DESC>"
           :module <MODULE-NAME>
           :controller <CONTROLLER-NAME>}
          (js/beforeEach (js/module "<MODULE-NAME>"))
          (js/beforeEach
           (js/inject
            (array "$rootScope" "$controller"
                   (fn [$rootScope $controller]
                     (! spec.$scope ($rootScope.$new))
                     ($controller "<CONTROLLER-NAME>" spec)))))
          (<FUNC> spec.$scope.<VAR>)
          (<FUNC> $ctrl.<VAR>))))



    (fact "describe.controller will generate this type of template:"
      (macroexpand-1
       '(describe.controller
         {:doc "<DESC>"
          :module <MODULE-NAME>
          :controller <CONTROLLER-NAME>
          :inject {:<V1> <V1-FORM>
                   :<V2> <V2-FORM>}}
         (<FUNC> $scope.<VAR>)
         (<FUNC> <V1>.<VAR>)
         (<FUNC> <V2>.<VAR>)))
      =>
      '(let [spec (js-obj)]
         (describe
          {:doc "<DESC>"
           :module <MODULE-NAME>
           :controller <CONTROLLER-NAME>
           :inject {:<V1> <V1-FORM>
                    :<V2> <V2-FORM>}}
          (js/beforeEach (js/module "<MODULE-NAME>"))
          (js/beforeEach
           (js/inject
            (array "$rootScope" "$controller" "<V1>" "<V2>"
                   (fn [$rootScope $controller <V1> <V2>]
                     (! spec.$scope ($rootScope.$new))
                     (! spec.<V1> <V1-FORM>)
                     (! spec.<V2> <V2-FORM>)
                     ($controller "<CONTROLLER-NAME>" spec)))))
          (<FUNC> spec.$scope.<VAR>)
          (<FUNC> spec.<V1>.<VAR>)
          (<FUNC> spec.<V2>.<VAR>))))
    ))
