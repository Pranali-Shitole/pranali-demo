def is_in_order(s):
 return s == ''.join(sorted(s))
is_in_order("edabit")
is_in_order("abc")
is_in_order("123")
is_in_order("xyzz")