(ns tutor)

(comment Primitives)

(type 1)

(type 1.1)

(type "Hi")

(type #"^\s$")

(type #inst "1980-03-05")

(type :key)

(type 'sym)

(type '(1 2 3))

(type [1 2 3])

(type {:a 1,:b 2})

(type #{1 2 3})

(comment LISP Syntax)

(comment

  "function (x){ return x + x }"

  ["function",["x"],["+","x","x"]]

  [function [x] [+ x x]]

  #_(fn [x]
      (+ x x)))

'(form arg-1 arg-2)

(comment
  form -> built-in
  form -> fn 
  form -> macro)

(+ 1 1)

(comment Build ins)

(def mysymbol 1)

(if true
  "true"
  "false")

(do (println "one")
    (println "two"))

(let [a 1]
  (+ a a))

(quote form)

(var symbol)

((fn [x] (* x x)) 5)

(loop [[x & xs] [1 2 3]]
  (when x
    (println x)
    (recur xs)))

(try
  (throw (Exception. "ups"))
  (catch Exception e
    "Error"))

(defn myfn
  "here is documentation"
  [x y] (+ (* x x) (* y y)))

(comment Macroexpand)

(defmacro unless [cond f t]
  `(if (not ~cond) ~f ~t))

(unless true 1 2)

(comment Composite types)

(map inc [1 2 3 4])

(reduce + 0 [1 2 3 4])

(map (fn [x] (* x x)) [1 2 3 4])

(for [x (range 10)]
  (+ 5 x))

(comment Arrow macro)

(->> (range 100)
     (filter odd?)
     (take 5))

(-> {}
    (assoc :a 1)
    (merge {:b 2}))

(comment Destructuring)

(let [{a :a b :b} {:a 1 :b 2}]
  (+ a b))

(let [[a b & c] [1 2 3 4]]
  (+ a b))

(comment Immutability)

(def coll [1 2 3])

(conj coll 4)

coll

(comment State)

(def state (atom []))

(type state)

(swap! state (fn [old] (conj old 1)))

(swap! state conj 2)

(conj @state 3)

(reset! state [])

(comment Interop)

(def date (java.util.Date.))

(.getHours date)

(import java.io.File)

(def file (java.io.File. "/etc/hosts"))

(.getPath file)

(.getName file)
