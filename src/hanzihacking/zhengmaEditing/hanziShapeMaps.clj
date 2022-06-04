(ns hanzihacking.zhengmaEditing.hanziShapeMaps)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])
(require '[hanzihacking.zhengmaEditing.zhengMaCleanedCharacterLists :refer [getVectorsWithCollisions tzaiSet getVectorsWithCollisions2]])

(defn hello [x] (println x))

(defn getIDSdata [pathStr] (map #(str/split (str/trim %) #"\s") (str/split (slurp (io/resource pathStr)) #"\n")))

;;(def tzaiSet13060 (tzaiSet "simpelCharFiles/zhenmaTzai13060noSpace.txt"))

(defn getIDSmap [pathStr]
  (apply hash-map (map #(if (coll? %) (nth (nth % 0) 2) %) (apply concat (group-by #(nth % 1) (getIDSdata pathStr))))))
;;(hash-map (vec (apply concat

(def test3 (getVectorsWithCollisions2 "simpelCharFiles/zhenmaTzai13060noSpace.txt"))
(def smallCollisions (vec (sort-by #(bigdec (get (get % 1) 0)) test3)))
(defn getCollFromIndex [x] (get (getIDSmap "rawFiles/ids.txt") (get (get (vec (apply concat smallCollisions)) x) 1)))
