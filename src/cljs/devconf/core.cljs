(ns devconf.core
  (:require [reagent.core :as reagent :refer [atom]]
            [garden.core :as css]
            [devconf.transport :as fmt]
            [devconf.transport :as tr]))


(defonce state (atom {}))


(defn chan-uri []
  (str "ws://"
       (.. js/window -location -host)
       "/chan"))


(defn send [raw-msg]
  (let [msg (fmt/to-transit raw-msg)]
    (when-let [s (:socket @state)]
      (.send s msg))))


(defn on-change [ev]
  (swap! state assoc :input (.. ev -target -value)))

(defn add-message [msg]
  (swap! state update-in [:items] conj msg))

(defn on-message [msg]
  (let [msg (fmt/from-transit msg)]
    (add-message msg)
    (.log js/console "Message" msg)))

(defn init-chan []
  (when-let [s (:socket @state)] (.close s))
  (let [s (js/WebSocket. (chan-uri))]
    (.log js/console "Socket " s)
    (aset s "onmessage" (fn [ev] (on-message (.-data ev))))
    (swap! state assoc :socket s)))

(defn on-submit [ev]
  (when (and (= 13 (.-which ev))
             (.-ctrlKey ev))
    (send {:message (:input @state)
                  :id (str (gensym))})
    (aset (.-target ev) "value" "")))

(defn $index []
  [:div#repl
   [:style (css/css [:textarea {:width "100%"
                                :min-height "100px"}])]
   [:textarea {:on-change on-change :on-key-down on-submit}]
   (for [i (:items @state)]
     [:div {:key (:id i)}
      [:pre (:message i) " => " (:result i)]
      [:hr]])])

(defn mount-root []
  (reagent/render
   [$index] (.getElementById js/document "app")))

(defn init! []
  (init-chan)
  (mount-root))

(comment

  (send "Hello server")

  )
