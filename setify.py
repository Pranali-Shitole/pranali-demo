def setify(lst):
 unique_set = set(sorted(lst))
 return list(unique_set)
setify([1, 3, 3, 5, 5]) 
setify([4, 4, 4, 4]) 
setify([5, 7, 8, 9, 10, 15])
setify([3, 3, 3, 2, 1])