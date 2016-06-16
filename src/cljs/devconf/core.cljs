(ns devconf.core
  (:require [reagent.core :as reagent :refer [atom]]
            [garden.core :as css]
            [devconf.transport :as tr]))


(defn $index []
  [:div#repl
   [:h1 "Hello"]])


(defn mount-root []
  (reagent/render
   [$index] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(comment

  )
