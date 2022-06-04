(ns hanzihacking.core)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])
(require '[hanzihacking.zhengmaEditing.zhengMaCleanedCharacterLists :refer [getVectorsWithCollisions tzaiSet getFromTzai getVectorsWithCollisions2]])
(require '[hanzihacking.zhengmaEditing.hanziShapeMaps :refer [hello getIDSdata getIDSmap]])
;(require '[hanzihacking.arrayEditing.genTzaiJundaFromArr :refer [getFromArray]])
;;getIDSdata


(def tradChar2 (take 9000 (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt")))


(def test3 (getVectorsWithCollisions2 "simpelCharFiles/zhenmaTzai13060noSpace.txt"))
;(println (count test3))
;(println (take 6 test3))

(def smallCollisions (vec (sort-by #(bigdec (get (get % 1) 0)) test3)))
;(println (count smallCollisions))
;(println (take 6 smallCollisions))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def test2 (getIDSmap "rawFiles/ids.txt"))
;(println (count test2))
;(println (take-last 50 test2))
;(println (get test2 "煃") )

(defn getCollFromIndex [x] (get test2 (get (get (vec (apply concat smallCollisions)) x) 1)))

(println (map #(getCollFromIndex %) (range 10)))
(println (get test2 "車"))

(def idsData (map #(vec (drop 1 %)) (getIDSdata "rawFiles/ids.txt")))
;(println (count idsData))
;(println (count idsData))
;(println (take 30 idsData))

(defn getAllIdsLine [idsChar] (vec (map #(get % 1) (filter #(= (get % 0) idsChar) idsData))))
;(println (getAllIdsLine  "車"))

;Jeg vil lave en map der har alle elementer i zhengma kortet
(defn allCodesWithChar [zmchar] (filter #(.contains (get % 2) zmchar) (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt")))
(println (count (allCodesWithChar "a")))
(println (take 5 (allCodesWithChar "a")))
(defn zmWithExactCharFromLetter [zmchar] (filter #(= (get % 2) zmchar) (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt")))
(defn zmWithExactCodeFromChar [zmchar] (filter #(= (get % 0) zmchar) (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt")))
;(defn zmWithMotherCharFromComponent [component] (filter #(get % ) test2))
(println (take 5 test2))

(defn getIdsElmsFromChar [x]
  (map #(vector (get % 0)
                (get % 1)
                (get % 2)
                (getAllIdsLine (get % 1))) (allCodesWithChar x)))
(println (count (getIdsElmsFromChar "co")))
(println (take 51 (getIdsElmsFromChar "co")))

(println (zmWithExactCharFromLetter "co"))
(println (zmWithExactCharFromLetter "ca"))
(println (zmWithExactCharFromLetter "cd"))
(println (getAllIdsLine "門"))
(println (zmWithExactCodeFromChar "門"))

;; legend: P (primary) S (secondary) O (other) 1 (first code) 2 (second Code) 3 (third code)
(def zmA {"一" "1Pa" "丁" "2Pai"})
(def zmB {"土" "1Pb" "士" "1Sb"
          "二" "2Pbd" "示" "2Pbk" "走" "2Pbo" "耂" "2Pbm" "者" "2Sbm"
          "工" "2Pbi" "亞" "2Pbz"})
(def zmC {"王" "1Pc" "三" "2Pcd"
          "玉" "2Pcs" "耳" "2Pce" "馬" "2Pcu" "髟" "2Pch" "長" "2Sch" "镸" "2Sch"
          "丰" "2Pci" "龶" "2Sci" "豐" "2Sci"
          "耒" "2Pck" "鬥" "2Pcc" "\uD845\uDDD7" "2Pco" "春" "2Sco"})
(def zmD {}) 

;壴   "丰" "2Sci"