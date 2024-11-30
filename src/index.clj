;; A Variety of Experiments
(ns index
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [nextjournal.clerk :as clerk]))

(defn experiment-paths []
  (-> "src/experiment"
      (fs/list-dir "*.clj")
      sort))

{:nextjournal.clerk/visibility {:result :show}}
^::clerk/no-cache
(clerk/html
 [:div
  [:ul
   (->> (experiment-paths)
        (map (fn [path]
               [:li [:a {:href (-> path
                                   str
                                   (str/replace ".clj" "")
                                   clerk/doc-url)}

                     (-> path
                         (.getFileName)
                         (str/replace ".clj" "")
                         (str/replace "_" "-"))]])))]])
