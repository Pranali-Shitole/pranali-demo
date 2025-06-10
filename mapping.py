def mapping(letters):
 result = {}
 for letter in letters:
  result[letter] = letter.upper()
 return result
mapping(["p", "s"]) 
mapping(["a", "b", "c"]) 
mapping(["a", "v", "y", "z"])