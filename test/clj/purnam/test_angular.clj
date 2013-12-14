(ns purnam.test-angular
  (:use [midje.sweet :exclude [contains]]
        purnam.checks
        purnam.angular))

(fact "inj-array"
  (inj-array '[<ARG1> <ARG2>])
  => '(array "<ARG1>" "<ARG2>"))

(fact "inj-fn"
  (inj-fn '[<ARG1> <ARG2>] '(<EXP1> <EXP2>))
  => '(array "<ARG1>" "<ARG2>"
         (fn [<ARG1> <ARG2>] <EXP1> <EXP2>)))

(fact "module-syms"
  (module-syms '<MODULE>)
  => (throws AssertionError)

  (module-syms '<MODULE>.<CTRL>)
  => ["<MODULE>" "<CTRL>"]

  (module-syms '<MODULE>.<NS>.<CTRL>)
  => ["<MODULE>.<NS>" "<CTRL>"])

(fact "def.module"
  (macroexpand-1
   '(def.module <MODULE>.<NS> []))
  => '(def <MODULE>_<NS>
        (.module js/angular "<MODULE>.<NS>" (array)))

  (macroexpand-1
   '(def.module <MODULE>.<NS> [<M1> <M1>.<NS>]))
  => '(def <MODULE>_<NS>
        (.module js/angular "<MODULE>.<NS>"
                 (array "<M1>" "<M1>.<NS>")))

  (macroexpand-1
   '(def.module my.app [ui ui.bootstrap]))
  => '(def my_app (.module js/angular "my.app"
                           (array "ui" "ui.bootstrap"))))

(fact "def.config"
  (macroexpand-1
   '(def.config <MODULE>.<NS> [<VAR1> <VAR2>] <EXP1> <EXP2>))
  => '(.config (.module js/angular "<MODULE>.<NS>")
               (array "<VAR1>" "<VAR2>"
                      (fn [<VAR1> <VAR2>] <EXP1> <EXP2>)))

  (macroexpand-1
   '(def.config my.app [$routeParams])))

(fact "value-fn"
  (value-fn '<MODULE>.<NS>.<VAL> '.<FUNC> '<BODY>)
  => '(do (def <MODULE>_<NS>_<VAL> <BODY>)
          (.<FUNC> (.module js/angular "<MODULE>.<NS>")
                   "<VAL>" <MODULE>_<NS>_<VAL>)))

(fact "function-fn"
  (function-fn '<MODULE>.<NS>.<VAL> '.<FUNC> '[<P1> <P2>]
             '(<EXP1> <EXP2>))
  '(do (def <MODULE>_<NS>_<VAL>
         (array "<P1>" "<P2>"
               (fn [<P1> <P2>] <EXP1> <EXP2>)))
       (.<FUNC> (.module js/angular "<MODULE>.<NS>")
                "<VAL>"
                <MODULE>_<NS>_<VAL>)))

(fact "angular-function"
  (angular-function 'controller)
  => '(defmacro def.controller [sym params & body]
        (function-fn sym (quote .controller) params body))
  (angular-function 'service)
  => '(defmacro def.service [sym params & body]
        (function-fn sym (quote .service) params body)))

(fact "angular-value"
  (angular-value 'value)
  => '(defmacro def.value [sym body]
        (value-fn sym (quote .value) body)))

(fact "def.service"
  (macroexpand-1
   '(def.service <MOD>.<SERVICE> [<VAR1> <VAR2>]
      <EXP1> <EXP2>))
  => '(do (def <MOD>_<SERVICE>
            (array "<VAR1>" "<VAR2>"
                   (fn [<VAR1> <VAR2>] <EXP1> <EXP2>)))
          (.service (.module js/angular "<MOD>")
                    "<SERVICE>" <MOD>_<SERVICE>)))

(fact "def.value"
  (macroexpand-1
   '(def.value <MOD>.<VALUE> <BODY>))
  => '(do (def <MOD>_<VALUE> <BODY>)
          (.value (.module js/angular "<MOD>")
                  "<VALUE>" <MOD>_<VALUE>)))
