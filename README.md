# hanzihacking

A Clojure library designed to ... well, that part is up to you.

## Usage

Key datastructures that can be imported:

(getFromTzai "simpelCharFiles/zhenmaTzai13060noSpace.txt"))
count; 13387
examples; ([1 的 nkrs] [2 是 kaii] [3 不 gi] [4 我 mdhm]

(getVectorsWithCollisions2 "simpelCharFiles/zhenmaTzai13060noSpace.txt")
count: 1057
examples: ([[6846 覞 lrlr] [9905 矎 lrlr]] [[6215 鴩 mbor] [12411 犪 mbor]] [[837 母 zy] [6293 毌 zy]]

(getIdsElmsFromChar "co") 
examples: ([679 具 lco [⿱⿴且一八[GTKV]]] [724 春 co [⿱𡗗日]] [922 奏 coag [⿱𡗗天]]

(getAllIdsLine "門") ;;;;; [⿰𠁣𠃛]

(zmWithExactCharFromLetter "co") ;;;; ([724 春 co])

(zmWithExactCodeFromChar "門")) ;;;;; ([400 門 xd])

## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
