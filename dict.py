def dict_to_list(input_dict):
 sorted_dict = sorted(input_dict.items())
 result = [(key, value) for key, value in sorted_dict]
 return result
dict_to_list({
 "D": 1, 
"B": 2, 
"C": 3
 })
dict_to_list({
 "likes": 2,
 "dislikes": 3,
 "followers": 10
 })