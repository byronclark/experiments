^{:nextjournal.clerk/visibility :hide-ns}
(ns experiment.airplane-seating)

;; ## Someone forgot
;;
;; A plane with N seats has N passengers with assigned seats. The first passenger lost
;; their boarding pass and picked a seat at random. Each subsequent passenger enters the
;; plane and sits at their assigned seat if it's available. If their assigned seat isn't
;; available, the passenger picks a random seat from the seats still available.
;;
;; What is the probability the Nth passenger's seat will still be available when entering
;; the plane?

{:nextjournal.clerk/visibility {:code :show :result :hide}}
(defn seat-passenger
  [available assigned]
  (if (and assigned
           (contains? available assigned))
    assigned
    (rand-nth (seq available))))

(defn simulate-seating
  "Seat n passengers with assigned seats.

  A `nil` in the assigned seats means the passenger lost their boarding pass.

  Returns a seq of [assigned taken] with an entry for each passenger."
  [n assignments]
  (->> assignments
       (reduce (fn [[seated available] assigned]
                 (let [seat-taken (seat-passenger available assigned)]
                   [(conj seated [assigned seat-taken])
                    (disj available seat-taken)]))
               [[] (set (range n))])
       first))

(defn simulations
  [passengers rounds gen-assignments]
  (reduce (fn [results _]
            (let [assignments (gen-assignments passengers)
                  [assigned taken] (-> passengers
                                       (simulate-seating assignments)
                                       last)
                  available? (= assigned taken)]
              (-> results
                  (update :total inc)
                  (update :success + (if available? 1 0)))))
          {:success 0 :total 0}
          (range rounds)))

;; Now let's find out how many times out of 5000 the last boarding passenger on a plane
;; with 100 seats will find their assigned seat available.
(defn first-lost-boarding-pass
  [n]
  (cons nil (rest (shuffle (range n)))))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(simulations 100 5000 first-lost-boarding-pass)

;; ## Everyone but the last forgot
;;
;; Now we want to see how likely the last person is to get their seat if everyone else lost their boarding passes.
{:nextjournal.clerk/visibility {:code :show :result :hide}}
(defn all-but-last-lost-boarding-pass
  [n]
  (conj (vec (repeat (dec n) nil))
        (rand-nth (range n))))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(simulations 100 5000 all-but-last-lost-boarding-pass)
