(ns hanzihacking.zhengmaEditing.hanziShapeMaps)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])
(require '[hanzihacking.zhengmaEditing.zhengMaCleanedCharacterLists :refer [getVectorsWithCollisions tzaiSet]])

(defn hello [x] (println x))

(defn getIDSdata [pathStr] (map #(str/split (str/trim %) #"\s") (str/split (slurp (io/resource pathStr)) #"\n")))

;;(def tzaiSet13060 (tzaiSet "simpelCharFiles/zhenmaTzai13060noSpace.txt"))

(defn getIDSmap [pathStr]
  (apply hash-map (map #(if (coll? %) (nth (nth % 0) 2) %) (apply concat (group-by #(nth % 1) (getIDSdata pathStr))))))
;;(hash-map (vec (apply concat