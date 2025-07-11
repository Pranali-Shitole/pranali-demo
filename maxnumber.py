numberlist=[12,3,55,23,6,78,33,4]
max=numberlist[0]
for num in numberlist:
    if max < num:
        max = num
        print(max)