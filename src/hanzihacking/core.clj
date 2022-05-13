(ns hanzihacking.core)
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; get tzai and junda maps

(defn getFromTzai
  [pathStr]
  (take 13060 (map #(clojure.string/trim (subs % 0 2)) (clojure.string/split (slurp (io/resource pathStr)) #"\r\n"))))

(defn getFromJunda
  [pathStr]
  (take 9933 (map #(nth % 1) (map #(clojure.string/split % #"\t") (clojure.string/split (slurp (io/resource pathStr)) #"\r\n")))))

(def tzaiChars (getFromTzai "rawFiles/tzai.txt"))
(println (count tzaiChars))
;;(println (last tzaiChars))

(def jundaChars (getFromJunda "rawFiles/junda.txt"))
;;(println (count jundaChars))
;;(println (take-last 5 jundaChars))

(def tzaiFreqMap
  (zipmap tzaiChars (take 13060 (range))))
;;(println (class (first tzaiChars)))
;;(println (take 10 tzaiFreqMap))
;;(println (get tzaiFreqMap "字"))
(def tzaiSet
  (set tzaiChars))

(def jundaSet
  (set jundaChars))

(def jundaFreqMap
  (zipmap jundaChars (take 9933 (range))))
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

(defn strEndInVV [x]
  (and (< 2 (count x)) (.endsWith x "vv")))
(defn strEndInaaa [x]
  (and (< 2 (count x)) (.endsWith x "aaa")))
(defn strEndInaa [x]
  (and (< 2 (count x)) (.endsWith x "aa")))
(defn strEndIn [x text]
  (and (< 2 (count x)) (.endsWith x text)))

(defn removeIfPresent [x text]
  (if (strEndIn x text) (subs x 0 (- (count x) (count text))) x))

(defn removeTestFromColl [coll text tooSmall]
  (map #(hash-map :code (if (< tooSmall (count (:code %)))
                          (removeIfPresent (:code %) text)
                          (:code %)) :freq (:freq %) :char (:char %)) coll))

(def zhengmaRawFreqList
       (filter #(some? (:freq %))
               (map #(hash-map
                       :char
                       (nth % 0)
                       :freq
                       (if (some? (get tzaiFreqMap (nth % 0))) (inc (get tzaiFreqMap (nth % 0))) nil)
                       :code
                       (nth % 1)
                       ) zhengmaChars)))        ;

(defn removeBadStrings [coll]
  (removeTestFromColl
    (removeTestFromColl
      (removeTestFromColl coll "vv" 2) "aaa" 3) "aa" 2))
(def zhengmaSansBadStrings (removeBadStrings zhengmaRawFreqList))
(println (count zhengmaSansBadStrings))
;(println (take 10 zhengmaSansBadStrings))

(def zhengmaCollectedByFreq
  (map #(hash-map :freq (nth % 0)
                  :entries (nth % 1)) (group-by #(:freq %) zhengmaSansBadStrings)))

;(println (count zhengmaCollectedByFreq))
;(println (take 10 zhengmaCollectedByFreq))

(defn longestCodeLenth [entryVec]
  (apply max (map #(count (:code %)) entryVec)))
(defn retainLongCodes [entryVec]
  (filter #(= (count (:code %)) (longestCodeLenth entryVec)) entryVec))
(def zhengmaOnlyLongest
  (sort-by :freq
    (flatten
      (map #(retainLongCodes %)
           (map #(:entries %) zhengmaCollectedByFreq)))))

(def zhengmaOnlyLongCollectedByFreq
  (sort-by #(:freq (get (:entries %) 0)) (map #(hash-map :freq (nth % 0)
                  :entries (nth % 1)) (group-by #(:freq %) zhengmaOnlyLongest))))
(def zhengmaOnlyLongCollectedByCode
  (sort-by #(:freq (get (:entries %) 0)) (map #(hash-map :code (nth % 0)
                  :entries (nth % 1)) (group-by #(:code %) zhengmaOnlyLongest))))

;(println (count zhengmaOnlyLongCollectedByFreq))
;(println (take 10 zhengmaOnlyLongCollectedByFreq))
(def strListZhengma
  (clojure.string/join "\n"
                       (vec
                         (map #(clojure.string/join " " [(.toString (:freq %)) (:char %) (:code %)])
                              (flatten
                                (map #(:entries %) zhengmaOnlyLongCollectedByFreq))))))

(println (count strListZhengma))
(println (take 10 strListZhengma))
(spit "zhenmaTzai13060noSpace.txt" strListZhengma)

;(println (count zhengmaOnlyLongCollectedByCode))
;(println (take 10 zhengmaOnlyLongCollectedByCode))

(def collisions (sort-by #(:freq (get (:entries %) 0)) (filter #(< 1 (count (:entries %))) zhengmaOnlyLongCollectedByCode)))
;(println (count collisions))
;(println (take 10 collisions))

;;;;;;;;;;clojure print unicode
(def testOutput (clojure.string/replace (.toString collisions) #"entries" "\n"))
;(spit "flubber2tzai13060.txt" testOutput)






