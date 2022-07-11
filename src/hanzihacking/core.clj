(ns hanzihacking.core)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])
(require '[hanzihacking.zhengmaEditing.zhengMaCleanedCharacterLists :refer [getVectorsWithCollisions tzaiSet getFromTzai getVectorsWithCollisions2]])
(require '[hanzihacking.zhengmaEditing.hanziShapeMaps :refer [hello getIDSdata getIDSmap getAllIdsLine getIdElemsWithStrInPosition
                                                              smallCollisions getCollFromIndex getExactIdElmFromChars
                                                              getIdsElmsFromChar zmWithExactCharFromLetter zmWithExactCodeFromChar]])


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
          "巠" "2Phd 2Pbi", "弋" "2Phs",;1 2Shd mising, looks like 弋 with a mising dot.
          "戈" "2Phm", "戊" "2Shm",  ;1 2Shm missing. looks like 戈 without dot.
          ;2 missing 2Shm looks like 戈 with 2 or 3 vertical strokes.
          ;1 2Phe missing. simplified form of car/vehicle
          "牙" "2Phi", "至" "2Phb"
          })
(def zmI {
          "虫" "1Pi", "卜" "2Pid", "乍" "2Pma 2Sid",
          "虍" "2Pih", "虎" "2Sih", "止" "2Pii", "龰" "2Sii",
          "齒" "2Sio" ;1 2Pio missing. simplified form of 齒
          })
(def zmJ {
          "口" "1Pj", "囗" "2Pjd", "因" "2Sjd",
          "足" "2Pji", "𧾷" "2Sji"
          })
(def zmK {
          "日" "1Pk", "曰" "1Sk", "⿻口一" "1Sk", ;the middle part of 衰
          "刂" "2Pkd", "业" "2Pku", "業" "2Sku", "婁" "2Pkj 2Pzm",
          "非" "2Pkc", "小" "2Pko", "⺌" "2Sko", "𣥂" "2Sko",
          "水" "2Pkv", "氺" "2Skv", "㡀" "2Skv 2Pld", "眔" "2Plk 2Skv",
          "田" "2Pki", "由" "2Pkia", "甲" "2Pkib", "申" "2Pkic"
          })
(def zmL {
          "目" "1Pl", "冂" "2Pld", "同" "2Sld", "𠔼" "2Sld", "冋" "2Sld", "冏" "2Sld", "⿵冂𢆉" "2Sld",
          "用" "2Sld", "甬" "2Sxs 2Sld", "角" "1Sr 2Sld", "𠕁" "2Sld", "岡" "2Sld",
          "巾" "2Pli", "山" "2Pll", "罒" "2Plk", "四" "2Slk", "曾" "2Pud 2Slk 1Pk", "會" "1Pod 1Ps 2Slk 1Pk",
          ;1 2Plk missing. looks like middle 曾 without the middle stroke
          "皿" "2Plka", "且" "2Plc", "⿴且一" "2Slc",
          "貝" "2Slo", "咼" "2Plj", "見" "2Slr", "骨" "2Plw"
          })
(def zmM {
          "竹" "1Pm", "⿱𠂉丶" "1Sm", "舌" "2Pmi", "𠂉" "2Pma", "矢" "2Sma",
          "攵" "2Pmo", "牛" "2Pmb", "失" "2Smb 2Pod", "𠂒" "2Smb", "㐄" "2Smb",
          "气" "2Pmy", "氣" "2Smy", "毛" "2Pmh", "禾" "2Pmf", "余" "2Pod 2Smf", "生" "2Pmc",
          "手" "2Pmd", "龵" "2Smd", "千" "2Pme"
          })
(def zmN {
          "亻" "1Pn", "片" "2Pnx", ; [2323 鼎 lznx [⿶⑧目]] lower left side, code: "2Snx",
          "川" "2Pnd", "⿰丿丨" "2Snd", ; 2Snd missing, looks like 丿丨+二
          "𣶒" "2Snd", "㐬" "2Psh 2Snd",
          "臼" "2Pnb", "臼丨" "2Snb", "與" "2Snb 1Po", "𦥯" "2Snb 2Pos 2Pos", "𦥑冖" "2Snb",
          "" "2Pnj"
          })

;龰22
;from file (switch) file (switch) (getAllIdsLine "門"))
(println (zmWithExactCodeFromChar "流"))
(println (zmWithExactCharFromLetter "mc"))

(defn prinAllStrings [strToSearch]
  (->>
    (println (take 600 (getIdsElmsFromChar strToSearch)));(getIdsElmsFromChar strToSearch));(take 100 (getIdsElmsFromChar strToSearch)))
    (println (zmWithExactCharFromLetter strToSearch))
    (println (getExactIdElmFromChars strToSearch))
    ))
(prinAllStrings "nb")

;(prinAllStrings "hdzi")
;(prinAllStrings "mhd")
;(t verticallyprinAllStrings "hdyd")
;(prinAllStrings "hs")
;(prinAllStrings "hx")

;帚 帚捃疌
;(pplitintln (zmWithExactCodeFplitomChar "巠"))
;(println (zmWithExactCodeFromChar "乇"))
;(println (zmWithExactCodeFromChar "切"))
;(println (zmWithExactCodeFromChar "疌"))

;(println (getIdElemsWithStrInPosition "d" 2))
;(println (getIdsElmsFromChar strToSearch))


;壴   "丰" "2Sci"