import re
string = "c o d e"
space = re.compile(r'\s+')
result = re.sub(space,"",string)
print(result)