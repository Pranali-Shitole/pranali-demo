vowel = ['a','e','i','o','u']
word = "programming"
count = 0
for character in word : 
    if character in vowel:
        count += 1
    print (count)