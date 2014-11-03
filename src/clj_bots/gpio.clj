(ns clj-bots.gpio)

(def edge-detection-modes #{:none :rising :falling :both})
(def states #{:high :low})
(def directions #{:input :output})

(defprotocol GPIOPin
  (pin [this])
  (direction [this])
  (set-direction! [this d] [this d state])
  (state [this])
  (set-state! [this s])
  (edge [this])
  (set-edge! [this e])
  (wait-for [this] [this timeout]))

(defn input?
  "test if the pin is current configured as an input pin."
  [p]
  (= (direction p) :input))

(defn input!
  "configure the pin to be an input."
  [p]
  (set-direction! p :input))

(defn output?
  "test if the pin is current configured as an output pin."
  [p]
  (= (direction p) :output))

(defn output!
  "configure the pin to be an output."
  [p]
  (set-direction! p :output))

(defn high?
  "test if the pin is currently :high."
  [p]
  (= (state p) :high))

(defn low?
  "test if the pin is currently :low."
  [p]
  (= (state p) :low))

(defn low!
  "set an output pin to :low."
  [p]
  (set-state! p :low))

(defn high!
  "set an output pin to :high."
  [p]
  (set-state! p :high))

(defn detect-rising? [p]
  (= (edge p) :rising))

(defn detect-falling? [p]
  (= (edge p) :falling))

(defn detect-both? [p]
  (= (edge p) :both))

(defn detect-none? [p]
  (= (edge p) :none))

(defn detect-rising! [p]
  (set-edge! p :rising))

(defn detect-falling! [p]
  (set-edge! p :falling))

(defn detect-both! [p]
  (set-edge! p :both))

(defn detect-none! [p]
  (set-edge! p :none))
