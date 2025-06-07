def double_char(input_str):
 doubled_str = ""
 for char in input_str:
  doubled_str += char * 2
 return doubled_str
double_char("String") 
double_char("Hello World!")
double_char("1234!_ ")