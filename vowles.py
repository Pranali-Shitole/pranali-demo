def replace_vowels(string, char):
 vowels = "AEIOUaeiou"  
 for vowel in vowels:
  string = string.replace(vowel, char)  
 return string
 print(replace_vowels("the aardvark", "#"))
 print(replace_vowels("minnie mouse", "?"))
 print(replace_vowels("shakespeare", "*"))