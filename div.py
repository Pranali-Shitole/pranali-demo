num1 = float(input("Enter the dividend by division:"))
num2 = float(input("Enter the divisor fordivision:"))
if num2 == 0:
    print("Error: Division by zero is not allowed.")
else:
    div_result = num1 / num2
    print(f"Division: {num1} / {num2}= {div_result}")
    