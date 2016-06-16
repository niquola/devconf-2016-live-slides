(ns perf
  (:require [immutant.web :refer :all]
            [ring.adapter.undertow :refer [run-undertow]]
            [org.httpkit.server :as http]
            [clojure.tools.logging :as log]))

(defn app [request]
  {:status 200
   :body "Hello world!"})

(defonce uto-srv (atom nil))

(defn start-uto []
  (reset! uto-srv
          (run-undertow app {:port 8081})))

(defn stop-uto []
  (when-let [s @uto-srv]
    (.stop s)))

(defonce hk-srv (atom nil))

(defn start-hk []
  (reset! hk-srv
          (http/run-server app {:port 8083})))

(defn stop-hk []
  (@hk-srv))

(defn start-all []
  (run app {:host "localhost" :port 8082 :path "/"})
  (start-uto)
  (start-hk))

(defn stop-all []
  (stop  {:host "localhost" :port 8082 :path "/"})
  (stop-uto)
  (stop-hk))

(comment
  (run app  {:host "localhost" :port 8082 :path "/"})

  (start-all)
  )


