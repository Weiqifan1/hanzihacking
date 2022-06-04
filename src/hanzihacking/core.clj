(ns hanzihacking.core)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])
(require '[hanzihacking.zhengmaEditing.zhengMaCleanedCharacterLists :refer [getVectorsWithCollisions tzaiSet getFromTzai getVectorsWithCollisions2]])
(require '[hanzihacking.zhengmaEditing.hanziShapeMaps :refer [hello getIDSdata getIDSmap getAllIdsLine
                                                              smallCollisions getCollFromIndex
                                                              getIdsElmsFromChar zmWithExactCharFromLetter zmWithExactCodeFromChar]])

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