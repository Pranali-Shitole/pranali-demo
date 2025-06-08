def missing_num(lst):
 total_sum = sum(range(1, 11))  
 given_sum = sum(lst)  
 missing = total_sum - given_sum
 return missing
missing_num([1, 2, 3, 4, 6, 7, 8, 9, 10])
missing_num([7, 2, 3, 6, 5, 9, 1, 4, 8])
missing_num([10, 5, 1, 2, 4, 6, 8, 3, 9])