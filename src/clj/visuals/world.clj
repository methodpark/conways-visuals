(ns visuals.world)

(def state (atom #{[0 0] [1 1] [2 2]}))

(defn update-world!
  [new-world]
  (let [new-world (if (set? new-world) new-world (set new-world))]
    (reset! state new-world)))

(defn get-world [] @state)
