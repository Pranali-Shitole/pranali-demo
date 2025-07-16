import re
name = 'python is 1'
digitCount = re.sub("[^0-9]","",name)
letterCount = re.sub("[â-zA-z]","",name)
spaceCount = re.findall("[ \s]",name)
print(len(digitCount))
print(len(letterCount))
print(len(spaceCount))