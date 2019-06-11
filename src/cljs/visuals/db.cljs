(ns visuals.db)

(def test-board #{[1 1] [2 2] [3 3]})

(def default-db
  {:name                  "re-frame"
   :board                 test-board
   :poll-server?          false
   :poll-server-intervall 3000})
