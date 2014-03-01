(ns purnam.checks)

(defn match-sym? [v]
  (and (symbol? v)
       (re-find #"^%" (str v))))

(defn match
  ([f1 f2] (match f1 f2 (atom {})))
  ([v1 v2 dict]
     (cond (or (and (list? v1) (list? v2))
            (and (vector? v1) (vector? v2)))
        (and (= (count v1) (count v2))
             (->> (map #(match %1 %2 dict) v1 v2)  
                  (every? true?)))

        (and (associative? v1) (associative? v2))
        (and (= (keys v1) (keys v2))
             (->> (map #(match %1 %2 dict) (vals v1) (vals v2))
                  (every? true)))

        (match-sym? v2)
        (if-let [vd (@dict v2)]
          (match v1 vd dict)
          (do (swap! dict assoc v2 v1)
              true))
        :else (= v1 v2))))
        
(defn matches [template]
  (fn [value]
    (match value template)))

(defn expands-into [result]
  (fn [form]
    (match (macroexpand-1 form) result)))