(ns hanzihacking.zhengmaEditing.zhengMaCleanedCharacterLists)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])


(defn getFromTzai
  [pathStr]
  (map #(str/split (str/trim %) #"\s") (str/split (slurp (io/resource pathStr)) #"\n")))

(defn tzaiSet [path] (set (map #(nth % 1) (getFromTzai path))))

(def tradChar (getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt"))

;;(println (count tradChar))
;;(println (take 10 tradChar))
(defn noDublicates [charVecVector]

  (< 1 (count (set (map #(nth % 1) charVecVector))))
  )

(def vectorsWithCollisions
  (filter #(noDublicates %)
          (map #(nth % 1)
               (filter
                 #(< 1 (count (nth % 1)))
                 (group-by #(nth % 2) tradChar)))))

(defn getVectorsWithCollisions [x]
  (let [tradChar (getFromTzai x)])
  (filter #(noDublicates %)
          (map #(nth % 1)
               (filter
                 #(< 1 (count (nth % 1)))
                 (group-by #(nth % 2) tradChar)))))


(defn noDublicates2 [charVecVector]
  (< 1 (count (set (map #(nth % 1) charVecVector))))
  )
(defn getVectorsWithCollisions2 [x]
  (let [tradChar (getFromTzai x)]
    (filter #(noDublicates2 %)                                ;
            (map #(nth % 1)                                   ;;;;;;
                 (filter
                   #(< 1 (count (nth % 1)))
                   (group-by #(nth % 2) tradChar))))))