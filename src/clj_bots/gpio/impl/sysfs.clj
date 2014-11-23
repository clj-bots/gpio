(ns clj-bots.gpio.impl.sysfs
  (:require [clj-bots.gpio :as gpio]
            [clojure.java.io :refer [file]]
            [net.n01se.clojure-jna.libc-utils :refer [select]]
            [net.n01se.clojure-jna :as jna]))

; (fopen "/path/to/file" "rw")
(def fopen (jna/to-fn Integer c/fopen))

; (fclose fd)
(def fclose (jna/to-fn Integer c/fclose))

(def sysfs-root (file "/sys/class/gpio"))

(defn keyword-to-state [k]
  (condp = k
    :high "1"
    :low "0"))

(defn state-to-keyword [s]
  (condp = s
    "1" :high
    "0" :low))

(defn keyword-to-direction [k]
  (condp = k
    :input "in"
    :output "out"))

(defn keyword-to-initial-state [k]
  (name k))

(defn direction-to-keyword [d]
  (condp = d
    "in" :input
    "out" :output))

(defn read-file [f]
  (trim-newline (slurp f)))

(defrecord SysfsGPIOPin
  [gpio-pin
   d-file
   v-file
   e-file
   al-file]
  gpio/GPIOPin
  (pin [this] gpio-pin)

  (direction [this]
    (let [content (read-file d-file)]
      (direction-to-keyword content)))

  (set-direction! [this d]
    (let [v (keyword-to-direction d)]
      (spit d-file v)))

  (set-direction! [this d state]
    (if (= d :output)
      (spit d-file (keyword-to-initial-state state))
      ; Blow up? input with an initial state doesn't make sense...
      ))

  (state [this]
    (let [content (read-file v-file)]
      (keyword-to-state content)))

  (set-state! [this s]
    (spit v-file (keyword-to-state s)))

  (edge [this]
    (keyword (read-file e-file)))

  (set-edge! [this e]
    (spit e-file (name e)))

  (wait-for [this]
    (wait-for this 0))

  ; could use some optimization re repeatedly openning/closing
  ; the file
  (wait-for [this timeout]
    (let [fd (fopen (.getAbsolutePath v-file) "r")
          result (select nil nil [fd] timeout)] ; blocking call
      (fclose fd)
      result))
  )

(defn reserve
  ([pin-id] (reserve sysfs-root))
  ([root pin-id]
   (spit (file root "export") pin-id)
   (let [pin-root (file root (str "gpio" pin-id))]
     (->SysfsGPIOPin
       (file pin-root "direction")
       (file pin-root "value")
       (file pin-root "edge")
       (file pin-root "active_low")))))

(defn release [p]
  (let [pin-id (cond
                 (integer? p) p
                 (instance? SysfsGPIOPin p) (pin p))]
    (spit (file root "unexport") pin-id)))
