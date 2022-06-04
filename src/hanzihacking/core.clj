(ns hanzihacking.core)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])
(require '[hanzihacking.zhengmaEditing.zhengMaCleanedCharacterLists :refer [getVectorsWithCollisions tzaiSet getFromTzai getVectorsWithCollisions2]])
(require '[hanzihacking.zhengmaEditing.hanziShapeMaps :refer [hello getIDSdata getIDSmap getAllIdsLine getIdElemsWithStrInPosition
                                                              smallCollisions getCollFromIndex getExactIdElmFromChars
                                                              getIdsElmsFromChar zmWithExactCharFromLetter zmWithExactCodeFromChar]])


;(println (getAllIdsLine "門"))
;(println (zmWithExactCodeFromChar "門"))

(def strToSearch "e")
(println (take 50 (getIdsElmsFromChar strToSearch)))
(println (zmWithExactCharFromLetter strToSearch))
(println (getExactIdElmFromChars strToSearch))
;(println (getIdElemsWithStrInPosition "d" 2))
;(println (getIdsElmsFromChar strToSearch))

;; legend: P (primary) S (secondary) T (tertiary/not in ZM chart) ... 1 (first code) 2 (second Code) 3 (third code)
(def zmA {"一" "1Pa" "丁" "2Pai"})
(def zmB {"土" "1Pb" "士" "1Sb"
          "二" "2Pbd" "示" "2Pbk" "走" "2Pbo" "耂" "2Pbm" "者" "2Sbm"
          "工" "2Pbi" "亞" "2Pbz"})
(def zmC {"王" "1Pc" "三" "2Pcd"
          "玉" "2Pcs" "耳" "2Pce" "馬" "2Pcu" "髟" "2Pch" "長" "2Sch" "镸" "2Sch"
          "丰" "2Pci" "龶" "2Sci" "豐" "2Sci"
          "耒" "2Pck" "鬥" "2Pcc" "𡗗" "2Pco" "春" "2Sco"})
(def zmD {"扌" "1Pd" "寸" "2Pds"}) ;missing 1Sd
(def zmE {"卌" "1Te"
          "艹" "1Pe" "廾" "1Se"}) ;missing 1Se

;壴   "丰" "2Sci"