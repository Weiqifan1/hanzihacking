(ns hanzihacking.core)
(require '[clojure.java.io :as io])


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
(println (take 10 zhengmaChars))

(def zhengmaRawFreqList
  (map #(list
          (nth % 0)
          (get tzaiFreqMap (nth % 0))
          (nth % 1)
          ) zhengmaChars))

(def zhengmaCleanList
  (map #(list (nth % 0) (inc (nth % 1)) (nth % 2))
    (filter #(and (some? (nth % 1))
                  (> 8001 (nth % 1))) zhengmaRawFreqList)))

(def cleanZhengmaFreqSet
  (set (map #(nth % 1) zhengmaCleanList)))
(println (class cleanZhengmaFreqSet))
(println (count cleanZhengmaFreqSet))
(println (count zhengmaRawFreqList))
(println (take 10 zhengmaCleanList))
(println (count zhengmaCleanList))

(def zhengmaSortedClean
  (sort-by #(nth % 1) zhengmaCleanList))

(println (take 50 zhengmaSortedClean))

