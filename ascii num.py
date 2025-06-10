def ascii_capitalize(input_str):
 result = ""
 for char in input_str:
  if ord(char) % 2 == 0:
   result += char.upper()
 else:
  result += char.lower()
 return result
ascii_capitalize("to be or not to be!")
ascii_capitalize("THE LITTLE MERMAID")
ascii_capitalize("Oh what a beautiful morning.")