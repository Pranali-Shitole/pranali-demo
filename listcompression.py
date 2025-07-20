squares = [x**2 for x in range(1, 6)]
print(squares)
evens = [x for x in range(1, 11) if x % 2 == 0]
print(evens)
words = ["apple", "banana", "cherry"]
upper_words = [word.upper() for word in words]
print(upper_words)  
matrix = [[1, 2], [3, 4], [5, 6]]
flattened = [num for row in matrix for num in row]
print(flattened)



