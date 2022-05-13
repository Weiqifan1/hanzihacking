(ns hanzihacking.core)
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; get tzai and junda maps

(defn getFromTzai
  [pathStr]
  (take 8000 (map #(clojure.string/trim (subs % 0 2)) (clojure.string/split (slurp (io/resource pathStr)) #"\r\n"))))

(defn getFromJunda
  [pathStr]
  (take 8000 (map #(nth % 1) (map #(clojure.string/split % #"\t") (clojure.string/split (slurp (io/resource pathStr)) #"\r\n")))))

(def tzaiChars (getFromTzai "rawFiles/tzai.txt"))
;;(println (count tzaiChars))
;;(println (last tzaiChars))

(def jundaChars (getFromJunda "rawFiles/junda.txt"))
;;(println (count jundaChars))
;;(println (take-last 5 jundaChars))

(def tzaiFreqMap
  (zipmap tzaiChars (take 8000 (range))))
;;(println (class (first tzaiChars)))
;;(println (take 10 tzaiFreqMap))
;;(println (get tzaiFreqMap "字"))
(def tzaiSet
  (set tzaiChars))

(def jundaSet
  (set jundaChars))

(def jundaFreqMap
  (zipmap jundaChars (take 8000 (range))))
;;(println (class (first jundaChars)))
;;(println (take 10 jundaFreqMap))
;;(println (get jundaFreqMap "字"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; get zhengma

(defn getFromZhengma
  [pathStr]
  (map #(list (nth % 0) (nth % 1))
       (map #(map clojure.string/trim (clojure.string/split % #"\s"))
            (map clojure.string/trim (clojure.string/split (slurp (io/resource pathStr)) #"\r\n")))))
(def zhengmaChars (getFromZhengma "rawFiles/zhengmaChar.txt"))
;(println (take 10 zhengmaChars))

(def zhengmaRawFreqList
  (map #(list
          (nth % 0)
          (get tzaiFreqMap (nth % 0))
          (nth % 1)
          ) zhengmaChars))

(def zhengmaCleanList
  (group-by #(nth % 1)
            (map #(list (nth % 0) (inc (nth % 1)) (nth % 2))
                 (filter #(and (some? (nth % 1))
                               (> 8001 (nth % 1))) zhengmaRawFreqList))))

(def cleanZhengmaFreqSet
  (set (map #(nth % 1) zhengmaCleanList)))
;(println (class cleanZhengmaFreqSet))
;(println (count cleanZhengmaFreqSet))
;(println (count zhengmaRawFreqList))
;(println (take 10 zhengmaCleanList))
;(println (count zhengmaCleanList))

(def zhengmaSortedClean
  (map #(nth % 1) (sort-by #(nth % 0) zhengmaCleanList)))
(println (take 50 zhengmaSortedClean))

(comment
  (defn strEndInVV [x]
    (and (< 2 (count x)) (.endsWith x "vv")))
  (defn entryCodeEndsInVV [x]
    (strEndInVV (nth x 2)))
  (defn entryVetorHasEntryEndInVV [x]
    (contains? (vec (map #(entryCodeEndsInVV %) x)) true)))

(println (take 5 zhengmaSortedClean))



;(def data_v [1 5 7])
;(println (sp/select sp/FIRST data_v))

;(def testsp
;  (map #(sp/select [(sp/walker string?)] %) zhengmaSortedClean))
;(println (take 10 testsp))

(comment
  (defn codeSize [x] (count (nth x 2)))
  (defn findMaxLen [x] (apply max (map #(codeSize %) x)))
  (defn findMaxCodelist [x] (filter #(= (codeSize %) (findMaxLen x)) x))
  (defn filterOnlyLargest [x] (map #(vector (nth % 0) (findMaxCodelist (nth % 1)))
                                   x))
  ;(println (take 18 (filterOnlyLargest zhengmaSortedClean)))
  (def onlyLargest (filterOnlyLargest zhengmaSortedClean))

  ;(def doubleCodes (filter #(< 1 (count (nth % 1))) onlyLargest))
  ;(println (take 10 doubleCodes))
  ;211

  (def onlyLargeFlatList (map #(flatten (nth % 1)) onlyLargest))
  (def seqOfCodes (seq (set (flatten (map #(nth % 2) onlyLargeFlatList)))))
  ;(def mapOfCodes)

  ;(def filterMathingCodes [x]
  ;  (filter #(= x (nth % 2)) onlyLargeFlatList))

  ;(println (take 10 onlyLargeFlatList))
  ;(println (take 10 codeToEntries))
  (defn filterMathingCodes [x] (filter #(= x (nth % 2)) onlyLargeFlatList))
  (def codeToEntries (map #(filterMathingCodes %) seqOfCodes))

  (def codeclashes (sort-by #(nth (nth % 0) 1) (filter #(< 1 (count %)) codeToEntries))))
;429
;;(println (take 10 codeclashes))