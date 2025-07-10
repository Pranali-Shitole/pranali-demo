def convert_string_to_decimals():
    input_str = input("Enter numbers (separated by space): ")
    try:
        float_list = [float(num) for num in input_str.split()]
        print("Decimal numbers:", float_list)
    except ValueError:
        print("Invalid input: please enter only numbers.")

convert_string_to_decimals()
