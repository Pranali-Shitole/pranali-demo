def list_operation(x, y, n):
 return [num for num in range(x, y + 1) if num % n == 0]
list_operation(1, 10, 3)
list_operation(7, 9, 2)
list_operation(15, 20, 7)