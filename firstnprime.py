def generate_primes(n):
    count = 0     
    num = 2       

    while count < n:
        if is_prime(num):
            yield num
            count += 1
        num += 1

def is_prime(x):
    if x < 2:
        return False
    for i in range(2, int(x**0.5) + 1):
        if x % i == 0:
            return False
    return True
n = int(input("Enter how many prime numbers you want: "))
for prime in generate_primes(n):
    print(prime, end=' ')
