(ns hanzihacking.zhengmaEditing.hanziShapeMaps)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])
(require '[hanzihacking.zhengmaEditing.zhengMaCleanedCharacterLists :refer [getVectorsWithCollisions
                                                                            getFromTzai
                                                                            tzaiSet
                                                                            getVectorsWithCollisions2]])

(defn hello [x] (println x))

(defn getIDSdata [pathStr] (map #(str/split (str/trim %) #"\s") (str/split (slurp (io/resource pathStr)) #"\n")))

;;(def tzaiSet13060 (tzaiSet "simpelCharFiles/zhenmaTzai13060noSpace.txt"))

(def idsData (map #(vec (drop 1 %)) (getIDSdata "rawFiles/ids.txt")))
(defn getIDSmap [pathStr]
  (apply hash-map (map #(if (coll? %) (nth (nth % 0) 2) %) (apply concat (group-by #(nth % 1) (getIDSdata pathStr))))))
;;(hash-map (vec (apply concat

(def test3 (getVectorsWithCollisions2 "simpelCharFiles/zhenmaTzai13060noSpace.txt"))
(def smallCollisions (vec (sort-by #(bigdec (get (get % 1) 0)) test3)))
(defn getCollFromIndex [x] (get (getIDSmap "rawFiles/ids.txt") (get (get (vec (apply concat smallCollisions)) x) 1)))
(defn getAllIdsLine [idsChar] (vec (map #(get % 1) (filter #(= (get % 0) idsChar) idsData))))
(defn allCodesWithChar [zmchar] (filter #(.contains (get % 2) zmchar) (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt")))
(defn zmWithExactCharFromLetter [zmchar] (filter #(= (get % 2) zmchar) (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt")))
(defn zmWithExactCodeFromChar [zmchar] (filter #(= (get % 1) zmchar) (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt")))
(defn getIdsElmsFromChar [x]
  (map #(vector (get % 0)
                (get % 1)
                (get % 2)
                (getAllIdsLine (get % 1))) (allCodesWithChar x)))

(defn getExactIdElmFromChars [x] (filter #(= (get % 2) x) (getIdsElmsFromChar x)))


(defn elemCodeHasLetterAtIndex [stringOfText letterStr position]
   (= position (str/index-of stringOfText letterStr)))

(defn elemCodeIsLongEnoughAndHasLetter [collStrAtIdx2 letterStr position]
  (and (<= (+ 1 position) (count (get collStrAtIdx2 2)))
       (elemCodeHasLetterAtIndex (get collStrAtIdx2 2) letterStr position)))

(defn getIdElemsWithStrInPosition [letter positionIndex]
  (filter #(elemCodeIsLongEnoughAndHasLetter % letter positionIndex) (getIdsElmsFromChar letter)))