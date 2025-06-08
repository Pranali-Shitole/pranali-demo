def next_in_line(lst, num):
 if lst:
  lst.pop(0)  
 lst.append(num)  
 return lst
 else:
 return "No list has been selected"
next_in_line([5, 6, 7, 8, 9], 1) 
next_in_line([7, 6, 3, 23, 17], 10)
next_in_line([1, 10, 20, 42 ], 6)
next_in_line([], 6)