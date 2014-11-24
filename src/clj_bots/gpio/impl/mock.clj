(ns clj-bots.gpio.impl.mock
  (:require [clj-bots.gpio :as gpio]))

(declare wait-for)

(defrecord MockGPIOPin
  [gpio-pin
   direction-val
   state-val
   edge-val]
  gpio/GPIOPin
  (pin [_] gpio-pin)

  (direction [_]
    @direction-val)

  (set-direction! [_ d]
    (swap! direction-val (fn [_] d)))

  (state [_]
    @state-val)

  (set-state! [_ s]
    (swap! state-val (fn [_] s)))

  (edge [_]
    @edge-val)

  (set-edge! [_ e]
    (swap! state-val (fn [_] e)))

  (wait-for [this]
    (wait-for this 0))

  ; Not yet implemented
  (wait-for [_ timeout]))

(defn reserve
  ([pin-id]
   (->MockGPIOPin
     pin-id
     (atom nil)
     (atom nil)
     (atom nil))))

(defn release [p])


