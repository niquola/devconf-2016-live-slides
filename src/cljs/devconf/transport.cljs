(ns devconf.transport
  (:require [cognitect.transit :as t]))

(defn from-transit [msg]
  (t/read (t/reader :json) msg))

(defn to-transit [msg]
  (t/write (t/writer :json) msg))

(from-transit (to-transit {:a 1 :b #{1 2 3}}))

(comment
  (from-transit (to-transit {:a 1})))

