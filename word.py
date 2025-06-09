def is_isogram(word):
 word = word.lower()
 unique_letters = set()
 for letter in word:
  if letter in unique_letters:
   return False
 unique_letters.add(letter)
 return True
is_isogram("Algorism")
is_isogram("PasSword")
is_isogram("Consecutive")