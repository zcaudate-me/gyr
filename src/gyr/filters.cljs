(ns gyr.filters
  (:require [goog.object :as o]
            [goog.array :as a]
            [purnam.native.functions :as j])
  (:use-macros [purnam.core :only [obj arr ! def.n]]
               [gyr.core :only [def.module def.filter]]))
  
 (defn augment-fn-string [func]
  (if (string? func)
     (fn [x]
        (j/aget-in x (st/split func #"\.")))
      func))

 (defn check-fn [func chk]
  (fn [x]
    (let [res (func x)]
      (if (fn? chk)
         (chk res)
         (= res chk)))))

(def.module gyr.filters [])

(def.filter gyr.filters.subArray []
  (fn [input start end]
    (let [out (if input (a/clone input) (arr))]
      (a/slice out start end))))

(def.filter gyr.filters.pr []
  (fn [input title]
    (js/console.log title input)
    input))

      
(def.filter gyr.filters.unique []
  (fn [input]
      (a/removeDuplicates input (arr))))

(def.filter gyr.filters.toArray []
  (fn [input]
    (a/toArray input)))

(def.filter gyr.filters.toObject []
  (fn [input kfunc]
    (a/toObject input kfunc)))

(def.filter gyr.filters.call []
  (fn [input func & args]
    (apply func input args)))

(def.filter gyr.filters.apply []
  (fn [input func args]
    (.apply func args)))

(def.filter gyr.filters.map []
  (fn [input func]
    (.map input (augment-fn-string func))))

(def.filter gyr.filters.filter []
  (fn 
    ([input func]
      (a/filter input (augment-fn-string func)))
    ([input func chk]
      (a/filter input 
        (check-fn (augment-fn-string func) chk)))))

(def.filter gyr.filters.take []
  (fn [input num]
    (a/slice input 0 num)))

(def.filter gyr.filters.drop []
  (fn [input num]
    (a/slice input num)))
    
(def.filter gyr.filters.flatten []
  (fn [input]
    (a/flatten input)))
        
(def.filter gyr.filters.count []
  (fn [input]
    (.-length input)))
    
(def.filter gyr.filters.sortBy []
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

(def.filter gyr.filters.change []
  (fn [input]
    (let [out (arr)]
      (input.forEach
        (fn [v] (out.push v)))
       out)))

(def.filter gyr.filters.partition []
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
(def.filter gyr.filters.groupBy []
  (fn 
    ([input func]
     (a/bucket input (augment-fn-string func)))
    ([input func chk]
     (a/bucket input 
      (check-fn (augment-fn-string func) chk)))))
      
