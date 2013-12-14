(ns purnam.test.angular
  (:require [clojure.string :as s]
            [purnam.js :refer [js-expand change-roots-map cons-sym-root]]
            [purnam.test :refer [describe-fn it-preprocess it-fn
                                 describe-default-options
                                 describe-roots-map]]))

(def l list)

(defn describe-parse-inject [form]
  (cond (symbol? form)
        {:var form :inj [form] :body form}

        (vector? form)
        (let [[v res] form]
          (cond (symbol? res)
                {:var v :inj [res] :body res}

                (list? res)
                {:var v :inj (first res) :body (second res)}))))

(defn describe-parse-injects [spec injects]
  (let [data (map describe-parse-inject injects)
        injs (set (mapcat :inj data))
        bfn (fn [v b] (l 'aset spec (str v) b))
        vdata (map :var data)
        bindings (map bfn vdata (map :body data))
        ivars (mapcat (fn [x] [x x]) vdata)]
    {:injs injs :bindings bindings :ivars ivars}))

(defn describe-ng-fn [options body]
  (let [options (merge describe-default-options options)
        {:keys [module spec inject]} options
        {:keys [injs ivars bindings]} (describe-parse-injects spec inject)
        rm (describe-roots-map spec ivars)]
    (describe-fn
     (dissoc options :inject :module)
     (apply list
            (l 'js/beforeEach
                    (l 'js/module (str module)))

            (l 'js/beforeEach
               (l 'js/inject
                  (concat (l 'array)
                          (map str injs)
                          (l (apply
                              l 'fn (apply vector injs)
                              bindings)))))

            (change-roots-map
             body rm)))))

(defmacro describe.ng [options & body]
  (describe-ng-fn options body))

(defn it-uses-fn [names desc body]
  (it-fn desc
         (l (l 'js/inject
               (concat (l 'array)
                       (map str names)
                       (l (concat (l 'fn names)
                                  body)))))))

(defmacro it-uses [names desc & body]
  (let [[desc body] (it-preprocess desc body)]
    (it-uses-fn names desc body)))

(defn it-uses-filter-fn [filt desc body]
  (it-uses-fn '[$filter]
              desc
              (l (apply l 'let [filt (l '$filter (str filt))]
                        body))))

(defmacro it-uses-filter [[filt] desc & body]
  (let [[desc body] (it-preprocess desc body)]
    (it-uses-filter-fn filt desc body)))

(defn it-compiles-fn [ele html desc body]
  (it-uses-fn '[$compile $rootScope]
              desc
              (l (apply l 'let [ele (l (l '$compile html) '$rootScope)]
                        body))))

(defmacro it-compiles [[ele html] desc & body]
  (let [[desc body] (it-preprocess desc body)]
    (it-compiles-fn ele html desc body)))

(defn describe-controller-fn [options body]
  (let [{:keys [controller inject]} options
        ninject (conj inject ['$scope (l ['$rootScope '$controller]
                               (l 'let ['scp# '($rootScope.$new)]
                                 (l '$controller (str controller) (l 'obj :$scope 'scp#))
                                 'scp#))])]
    (describe-ng-fn (-> options (assoc :inject ninject) (dissoc :controller)) body)))

(defmacro describe.controller [options & body]
  (describe-controller-fn options body))
