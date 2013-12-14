(ns purnam.angular
  (:use [purnam.js :only [js-expand]])
  (:require [clojure.string :as s]))

(defn inj-array [params]
  (cons 'array (map str params)))

(defn inj-fn [params body]
  (concat (cons 'array
                (if (empty? params) []
                  `(~@(map str params))))
          [(concat ['fn params]
                   (if (empty? body) nil
                       `(~@body)))]))

(defn module-syms [sym]
  (let [symbols    (s/split (str sym) #"\.")
        _          (assert  (<= 2 (count symbols))
                            "The controller must be defined in
                           the form [<module>.<optional-ns>].<controller>")
        ctrl       (last symbols)
        module     (s/join "." (butlast symbols) )]
    [module ctrl]))

(defn angular-module
  ([ss] (list '.module 'js/angular ss))
  ([ss params] (list '.module 'js/angular ss (inj-array params))))

(defmacro def.module [sym params]
  (let [ss       (str sym)
        symbols  (s/split ss #"\.")]
    (list 'def (symbol (s/join "_" symbols))
          (angular-module ss params))))

(defmacro def.config [mod params & body]
  (list '.config (angular-module (str mod)) (inj-fn params (js-expand body))))

(defn value-fn [sym f body]
  (let [[module ctrl] (module-syms sym)
        dsym (symbol (s/join "_" (s/split (str sym) #"\.")))]
    (list 'do
          (list 'def dsym body)
          (list f (angular-module module)
                ctrl dsym))))

(defn function-fn [sym f params body]
  (let [fn-body (inj-fn params (js-expand body))]
    (value-fn sym f fn-body)))

(defn angular-function [f]
  (list 'defmacro (symbol (str "def." f)) '[sym params & body]
        (list 'function-fn 'sym (list 'quote (symbol (str "." f))) 'params 'body)))

(defn angular-value [f]
  (list 'defmacro (symbol (str "def." f)) '[sym body]
        (list 'value-fn 'sym (list 'quote (symbol (str "." f))) 'body)))

(defmacro angular.functions [v]
  (apply list 'do (map #(angular-function %) v)))

(defmacro angular.values [v]
  (apply list 'do (map #(angular-value %) v)))

(angular.functions [controller service factory provider filter directive])
(angular.values [constant value])
