(ns purnam.angular.filters
  (:use [purnam.native :only [aget-in aset-in augment-fn-string check-fn]])
  (:require [goog.object :as o]
            [goog.array :as a])
  (:require-macros [purnam.core :as j])
  (:use-macros [purnam.core :only [obj arr ! def.n]]
               [purnam.angular :only [def.module def.filter]]))

(def.module purnam.filters [])

(def.filter purnam.filters.subArray []
  (fn [input start end]
    (let [out (if input (a/clone input) (arr))]
      (a/slice out start end))))

(def.filter purnam.filters.pr []
  (fn [input title]
    (js/console.log title input)
    input))

      
(def.filter purnam.filters.unique []
  (fn [input]
      (a/removeDuplicates input (arr))))

(def.filter purnam.filters.toArray []
  (fn [input]
    (a/toArray input)))

(def.filter purnam.filters.toObject []
  (fn [input kfunc]
    (a/toObject input kfunc)))

(def.filter purnam.filters.call []
  (fn [input func & args]
    (apply func input args)))

(def.filter purnam.filters.apply []
  (fn [input func args]
    (.apply func args)))

(def.filter purnam.filters.map []
  (fn [input func]
    (.map input (augment-fn-string func))))

(def.filter purnam.filters.filter []
  (fn 
    ([input func]
      (a/filter input (augment-fn-string func)))
    ([input func chk]
      (a/filter input 
        (check-fn (augment-fn-string func) chk)))))

(def.filter purnam.filters.take []
  (fn [input num]
    (a/slice input 0 num)))

(def.filter purnam.filters.drop []
  (fn [input num]
    (a/slice input num)))
    
(def.filter purnam.filters.flatten []
  (fn [input]
    (a/flatten input)))
        
(def.filter purnam.filters.count []
  (fn [input]
    (.-length input)))
    
(def.filter purnam.filters.sortBy []
  (fn 
    ([input func]
      (let [f   (augment-fn-string func)
            out (a/clone input)]
        (.sort out
          (fn [a b]
            (> (f a) (f b))))
        out))

    ([input func rev]
      (let [f   (augment-fn-string func)
            out (a/clone input)]
        (a/sort out
          (fn [a b]
            (< (f a) (f b))))
        out))))

(def.filter purnam.filters.change []
  (fn [input]
    (let [out (arr)]
      (input.forEach
        (fn [v] (out.push v)))
       out)))

(def.filter purnam.filters.partition []
  (fn [input n]
      (loop [i    0
             j    -1
             out  (arr)]
        (cond (>= i input.length) out
  
              (= 0 (mod i n))
              (let [oarr (arr input.|i|)]
                (! input.|i|.$$hashKey (str "A" i))
                (! oarr.$$hashKey (str "A" i))
                (out.push oarr)
                (recur (inc i) (inc j) out))
              
              :else
              (do (! input.|i|.$$hashKey (str "A" i))
                  (out.|j|.push input.|i|)
                  (recur (inc i) j out))))))
;; TODO
(def.filter purnam.filters.groupBy []
  (fn 
    ([input func]
     (a/bucket input (augment-fn-string func)))
    ([input func chk]
     (a/bucket input 
      (check-fn (augment-fn-string func) chk)))))
      
