def compound_interest(p, t, r, n):
 a = p * (1 + (r / n)) ** (n * t)
 return round(a, 2)
compound_interest(10000, 10, 0.06, 12)
compound_interest(100, 1, 0.05, 1)
compound_interest(3500, 15, 0.1, 4)
compound_interest(100000, 20, 0.15, 365)