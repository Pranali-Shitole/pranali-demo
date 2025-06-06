def filter_list(lst):
 result = []
 for element in lst:
  if isinstance(element, int) and element >= 0:
   result.append(element)
 return result
 filter_list([1, 2, "a", "b"])
 filter_list([1, "a", "b", 0, 15])
 filter_list([1, 2, "aasf", "1", "123", 123])