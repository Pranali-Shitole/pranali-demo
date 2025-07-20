from functools import reduce
factorial = lambda n: reduce(lambda x, y: x * y, range(1, n + 1)) if n > 0 else 1
print(factorial(5))  
