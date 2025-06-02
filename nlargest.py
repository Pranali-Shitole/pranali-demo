def find_n_largest_elements(lst, n):
 sorted_lst = sorted(lst, reverse=True)
 largest_elements = sorted_lst[:n]
 return largest_elements
 N = int(input("N = " ))
 numbers = [30, 10, 45, 5, 20, 50, 15, 3, 345, 54, 67, 87, 98, 100, 34]
 result = find_n_largest_elements(numbers, N)
 print(f"The {N} largest elements in the list are:", result)