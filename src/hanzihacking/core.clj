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

(defn prinAllStrings [strToSearch]
  (->>
    (println (take 100 (getIdsElmsFromChar strToSearch)))
    (println (zmWithExactCharFromLetter strToSearch))
    (println (getExactIdElmFromChars strToSearch))
    ))
(prinAllStrings "hd")
;(prinAllStrings "hdzi")
;(prinAllStrings "mhd")
;(prinAllStrings "hdyd")
(prinAllStrings "hs")
;(prinAllStrings "hx")

;帚 帚捃疌
(println (zmWithExactCodeFromChar "巠"))
;(println (zmWithExactCodeFromChar "乇"))
;(println (zmWithExactCodeFromChar "切"))
;(println (zmWithExactCodeFromChar "疌"))

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
          "艹" "1Pe" "廾" "1Se"
          "十" "2Ped" "革" "2Pee"
          "廿" "2Pea" "龷" "2Sea" "卌一" "2Sea" "⑤" "2Sea" ;[455 帶 eawl [⿳⑤冖巾]]
          "甘" "2Peb" ;missin 2Seb
          "其" "2Pec" "栽" "2Peh" "𢦏" "2Seh"
          })
(def zmF {"木" "1Pf" "酉" "2Pfd" "覀" "2Pfj" "西" "2Sfj"
          "車" "2Pfk" "⑧" "2Sfk" ;[429 專 fkds [⿱⑧寸]]
          "甫" "2Pfb" "雨" "2Pfv"
          })
(def zmG {"石" "1Pg" "丆" "1Sg" "厂" "2Pgg" "大" "2Pgd" "𠂇" "2Sgd"
          "辰" "2Pgh" "尨" "2Sgm" ;the implified dragon harater is missing
          "不" "2Pgi" "頁" "2Sgo" ;simplified 頁 is missin
          "而" "2Pgl"
          "豕" "2Pgq" "𧰨" "2Sgq" ;1 2Sgq mising
          })
(def zmH {
          "匚" "1Ph", "臣" "1Sh", "一⿰𠄌⿺乀丿" "1Sh" ;[150 長 cha [⿳④一⿰𠄌⿺乀丿]]  ;2 1Sh element mangler (ligner 臣 men 口 i midten) og (ligner spejlvent 彐)
          ,"七" "2Phd", "" "2Phd",
          "⿻丿𡳾" "2Phd 2Pzi", ;[3043 屯 hdzi [⿻丿𡳾]]
          "巠" "2Phd 2Pbi", "弋" "2Phs" ;1 2Shd mising, looks like 弋 with a mising dot.
          })



;壴   "丰" "2Sci"