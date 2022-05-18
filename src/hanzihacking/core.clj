(ns hanzihacking.core)
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[com.rpl.specter :as sp])
(require '[clojure.data.json :as json])
(require '[hanzihacking.zhengMaCleanedCharacterLists :refer [getVectorsWithCollisions tzaiSet]])
(require '[hanzihacking.hanziShapeMaps :refer [hello getIDSdata getIDSmap]])
;;getIDSdata

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; zhengma vecors (1057) with colliions)
;(def vecorsWithCollisions (getVectorsWithCollisions "simpelCharFiles/zhenmaTzai13060noSpace.txt"))
;(println (count vecorsWithCollisions))
;(println (take 6 vecorsWithCollisions))
;;;;;;;;;;;;;;;;;;;;;;;


(def test2 (getIDSmap "rawFiles/ids.txt"))

(println (count test2))
(println (take-last 10 test2))
(println (get test2 "煃"))




