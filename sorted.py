def is_triplet(a, b, c):
 sorted_numbers = sorted([a, b, c])
 return sorted_numbers[0] ** 2 + sorted_numbers[1] ** 2 == sorted_numbers
is_triplet(3, 4, 5)
is_triplet(13, 5, 12)
is_triplet(1, 2, 3)