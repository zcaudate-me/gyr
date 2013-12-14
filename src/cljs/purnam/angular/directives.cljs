(ns purnam.angular.directives
  (:use [purnam.native :only [aget-in aset-in]])
  (:require-macros [purnam.core :as j])
  (:use-macros [purnam.core :only [obj ! def.n]]
               [purnam.angular :only [def.module def.directive]]))

(def.module purnam [])

(def.directive purnam.ngBlur [$parse]
  (fn [scope elem attrs]
    (let [f ($parse attrs.ngBlur)]
      (elem.bind
       "blur"
       (fn [e]
         (scope.$apply (fn [] (f scope (obj :$event e)))))))))

(def.directive purnam.ngFocus [$parse]
  (fn [scope elem attrs]
    (let [f ($parse attrs.ngFocus)]
      (elem.bind
       "focus"
       (fn [e]
         (scope.$apply (fn [] (f scope (obj :$event e)))))))))

(def.directive purnam.ngLet []
  (obj
    :scope false
    :link 
    (fn [$scope $element $attr]
      (let [regex #"^\s*(.*)\s+as\s+(.*)\s*"]
        (doseq [line ($attr.ngLet.split ";")]
          (if-let [match (line.match regex)]
            (let [varName match.1
                  varExp  match.2
                  assign  
                  (fn [value] 
                    (! $scope.|varName| value))]
               (assign ($scope.$eval varExp))
               ($scope.$watch varExp assign))))))))
