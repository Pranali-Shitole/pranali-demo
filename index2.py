def filter_list(lst):
 return [x for x in lst if isinstance(x, int)]
filter_list([1, 2, 3, "a", "b", 4]) 
filter_list(["A", 0, "Edabit", 1729, "Python", 1729]) 
filter_list(["A", 0, "Edabit", 1729, "Python", 1729])
filter_list(["Nothing", "here"])