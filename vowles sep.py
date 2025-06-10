def vow_replace(string, vowel):
 vowels = "aeiou"
 result = ""
 for char in string:
  if char in vowels:
   result += vowel
 else:
  result += char
 return result
vow_replace("apples and bananas", "u")
vow_replace("cheese casserole", "o")
vow_replace("stuffed jalapeno poppers", "e")