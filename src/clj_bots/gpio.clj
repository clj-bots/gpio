(ns clj-bots.gpio)

(def edge-detection-modes #{:none :rising :falling :both})
(def states #{:high :low})
(def directions #{:input :output})

(defprotocol GPIOPin
  (pin [this])
  (direction [this])
  (set-direction! [this d] [this d state])
  (state [this])
  (set-state! [this s]))

(defn input?
  "Test if the pin is current configured as an input pin."
  [p]
  (= (direction p) :input))

(defn input!
  "Configure the pin to be an input."
  [p]
  (set-direction! p :input))

(defn output?
  "Test if the pin is current configured as an output pin."
  [p]
  (= (direction p) :output))

(defn output!
  "Configure the pin to be an output."
  [p]
  (set-direction! p :output))

(defn high?
  "Test if the pin is currently :high."
  [p]
  (= (state p) :high))

(defn low?
  "Test if the pin is currently :low."
  [p]
  (= (state p) :low))

(defn low!
  "Set an output pin to :low."
  [p]
  (set-state! p :low))

(defn high!
  "Set an output pin to :high."
  [p]
  (set-state! p :high))
