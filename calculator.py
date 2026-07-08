import math

def display_menu():
    print("\n" + "=" * 40)
    print("        🧮  PYTHON CALCULATOR")
    print("=" * 40)
    print("  1. ➕  Addition       (a + b)")
    print("  2. ➖  Subtraction    (a - b)")
    print("  3. ✖️   Multiplication  (a × b)")
    print("  4. ➗  Division       (a ÷ b)")
    print("  5. 🔢  Modulus        (a % b)")
    print("  6. 🔺  Exponentiation (a ^ b)")
    print("  7. √   Square Root    (√a)")
    print("  8. 🚪  Exit")
    print("=" * 40)


def get_number(prompt):
    """Prompt the user and return a valid float."""
    while True:
        try:
            return float(input(prompt))
        except ValueError:
            print("  ⚠️  Invalid input! Please enter a numeric value.")


def get_two_numbers():
    """Get two numbers from the user."""
    a = get_number("  Enter first number  : ")
    b = get_number("  Enter second number : ")
    return a, b


def addition():
    a, b = get_two_numbers()
    result = a + b
    print(f"\n  ✅  {a} + {b} = {result}")


def subtraction():
    a, b = get_two_numbers()
    result = a - b
    print(f"\n  ✅  {a} - {b} = {result}")


def multiplication():
    a, b = get_two_numbers()
    result = a * b
    print(f"\n  ✅  {a} × {b} = {result}")


def division():
    a, b = get_two_numbers()
    if b == 0:
        print("\n  ❌  Error: Division by zero is not allowed!")
        return
    result = a / b
    print(f"\n  ✅  {a} ÷ {b} = {result}")


def modulus():
    a, b = get_two_numbers()
    if b == 0:
        print("\n  ❌  Error: Modulus by zero is not allowed!")
        return
    result = a % b
    print(f"\n  ✅  {a} % {b} = {result}")


def exponentiation():
    a, b = get_two_numbers()
    result = a ** b
    print(f"\n  ✅  {a} ^ {b} = {result}")


def square_root():
    a = get_number("  Enter number : ")
    if a < 0:
        print("\n  ❌  Error: Square root of a negative number is not defined in real numbers!")
        return
    result = math.sqrt(a)
    print(f"\n  ✅  √{a} = {result}")


def main():
    print("\n  Welcome to the Python Calculator!")

    operations = {
        "1": addition,
        "2": subtraction,
        "3": multiplication,
        "4": division,
        "5": modulus,
        "6": exponentiation,
        "7": square_root,
    }

    while True:
        display_menu()
        choice = input("  Select an option (1-8): ").strip()

        if choice == "8":
            print("\n  👋  Thank you for using Python Calculator. Goodbye!\n")
            break
        elif choice in operations:
            operations[choice]()
        else:
            print("\n  ⚠️  Invalid choice! Please select a valid option (1-8).")


if __name__ == "__main__":
    main()
