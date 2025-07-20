def sum_all(*args):
    total = sum(args)
    print("Sum:", total)

sum_all(1, 2, 3)
sum_all(10, 20)
def print_details(**kwargs):
    for key, value in kwargs.items():
        print(f"{key}: {value}")

print_details(name="Alice", age=30, job="Engineer")
