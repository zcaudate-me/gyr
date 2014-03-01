(ns gyr.directives
  (:require-macros [purnam.core :as j])
  (:use-macros [purnam.core :only [obj ! def.n]]
               [gyr.core :only [def.module def.directive]]))

(def.module gyr [])

(def.directive gyr.ngBlur [$parse]
  (fn [scope elem attrs]
    (let [f ($parse attrs.ngBlur)]
      (elem.bind
       "blur"
       (fn [e]
         (scope.$apply (fn [] (f scope (obj :$event e)))))))))

(def.directive gyr.ngFocus [$parse]
  (fn [scope elem attrs]
    (let [f ($parse attrs.ngFocus)]
      (elem.bind
       "focus"
       (fn [e]
         (scope.$apply (fn [] (f scope (obj :$event e)))))))))

(def.directive gyr.ngLet []
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
