def square_digits(n):
 num_str = str(n)
 result_str = ""
 for digit in num_str:
  squared_digit = int(digit) ** 2
 result_str += str(squared_digit)
 return int(result_str)
square_digits(9119)
square_digits(3212)
square_digits(2483)