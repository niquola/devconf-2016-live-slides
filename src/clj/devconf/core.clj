(ns devconf.core
  (:require [org.httpkit.server :as http]
            [hiccup.core :as html]
            [ring.middleware.defaults :as mw]
            [route-map.core :as rt]
            [devconf.transport :as fmt]
            [garden.core :as css]))

(defn style []
  (css/css
   [:body {:padding "18px"}]))

(defn js [src]
  [:script {:src src :type "text/javascript"}])

(defn layout [cnt]
  (html/html
   [:html
    [:head
     [:title "DevConff"]
     #_[:style (style)]]
    [:body cnt
     (js "/js/app.js")]]))

(defn index [req]
  {:body (layout [:div#app
                  [:p "Loading.."]])
   :headers {"Content-Type" "text/html"}
   :status 200})

(defonce clinets (atom #{}))

(defn broad-cast [msg]
  (let [s (fmt/to-transit msg)]
    (doseq [c @clinets]
      (http/send! c s))))

(defn do-eval [f]
  (with-out-str
    (println
     (try (eval (read-string f))
          (catch Exception e
            (pr-str e))))))

(do-eval "(println 1)")

(defn on-message [raw-msg]
  (let [msg (fmt/from-transit raw-msg)
        msg (assoc msg :result (do-eval (:message msg)))]
    (println "message " msg)
    (broad-cast msg)))

(defn chan [req]
  (http/with-channel req ch
    (println "Incomming connection" ch)
    (swap! clinets conj ch)
    (http/on-receive ch #'on-message)
    (http/on-close ch (fn [_] (swap! clinets disj ch)))))

(def routes {:GET #'index
             "chan" {:GET #'chan}})

(defn dispatch [{uri :uri meth :request-method :as req}]
  (if-let [r (rt/match [meth uri] routes)]
    ((:match r) req)
    {:body "Not found" :status 404 :headers {"Content-Type" "text"}}))


(def app (-> dispatch
             (mw/wrap-defaults mw/site-defaults)))

(defonce stop (atom nil))

(defn start []
  (when-let [s @stop] (s))
  (reset! stop
          (http/run-server #'app {:port 8080})))

(comment
  (start)
  (@stop)
  )
