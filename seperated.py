def multiply_nums(nums_str):
 nums = [int(num) for num in nums_str.split(", ")]
 result = 1
 for num in nums:
  result *= num
 return result
multiply_nums("2, 3")
multiply_nums("1, 2, 3, 4")
multiply_nums("54, 75, 453, 0")
multiply_nums("10, -2")