def find_duplicates(input_str):
 char_count = {}
 duplicates = []
 for i in input_str: 
  if i in char_count:
   char_count[i] += 1
 else:
  char_count[i] = 1
 return duplicates 
for i, count in char_count.items():
 if count > 1:
  duplicates.append(i)
 input_string = "piyush sharma"
 duplicate_chars = find_duplicates(input_string)
 print("Duplicate characters:", duplicate_chars)