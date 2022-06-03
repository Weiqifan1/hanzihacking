(ns hanzihacking.arrayEditing.genTzaiJundaFromArr)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])



(defn getFromArray
  [pathStr]
  (map #(list (nth % 0) (nth % 1))
       (map #(map clojure.string/trim (clojure.string/split % #"\s"))
            (map clojure.string/trim (clojure.string/split (slurp (io/resource pathStr)) #"\n")))))


;;;;;;;array test
(def test1 (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt"))
;;;;(def test1 (take 9000 (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt")))
(println (count test1))
(println (take 6 test1))
(def simpleTzaiSet (set (map #(get % 1) test1)))
(println (count simpleTzaiSet))
(println (take 6 simpleTzaiSet))

;;;;;; array code ;;;;;;;;;;
(def arrRes (map #(vec %) (getFromArray "rawFiles/array30_27489CHR.txt")))
(println (count arrRes))
(println (take 5 arrRes))

(def only13060 (filter #(contains? simpleTzaiSet (get % 1)) arrRes))
(println (count only13060))
(println (take 2 only13060))

(def codeGroup (filter #(< 1 (count (get % 1))) (group-by #(get % 0) only13060)))
(println (count codeGroup))
(println (take 5 codeGroup))

;;;; result:
;13387
;([1 的 nkrs] [2 是 kaii] [3 不 gi] [4 我 mdhm] [5 一 a] [6 有 gdq])
;13060
;(昱 羉 洗 零 蹁 嫢)
;31585
;([﻿CPU 溫] [WPU 媼] [G.; 啟] [FLVL 莠] [SLH 丸])
;15163
;([﻿CPU 溫] [WPU 媼])
;1205
;([CKY [[CKY 雀] [CKY 淮]]] [QS [[QS 軋] [QS 旡] [QS 匹]]] [.BMS [[.BMS 亂] [.BMS 覶]]] [ARH [[ARH 玉] [ARH 玊]]] [KJM [[KJM 貧] [KJM 偩]]])
