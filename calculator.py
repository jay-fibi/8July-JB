"""
calculator.py
=============
A feature-rich command-line calculator written in Python.

Supported operations:
    1. Addition        (a + b)
    2. Subtraction     (a - b)
    3. Multiplication  (a × b)
    4. Division        (a ÷ b)
    5. Modulus         (a % b)
    6. Exponentiation  (a ^ b)
    7. Square Root     (√a)
    8. Exit

Usage:
    python calculator.py

Features:
    - Interactive menu-driven interface with emoji icons.
    - Robust input validation with helpful error messages.
    - Guards against division/modulus by zero.
    - Guards against square root of negative numbers.

Author:  Jay Gohil
Version: 1.0
"""

import math


def display_menu():
    """
    Print the calculator's main menu to the console.

    Displays all available operation options (1-8) along with
    their corresponding symbols and descriptions.
    """
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
    """
    Prompt the user for input and return a valid floating-point number.

    Keeps re-prompting until the user enters a value that can be
    successfully converted to a float.

    Args:
        prompt (str): The message displayed to the user before input.

    Returns:
        float: The validated numeric value entered by the user.
    """
    while True:
        try:
            return float(input(prompt))
        except ValueError:
            # Inform the user and loop again for a valid numeric entry
            print("  ⚠️  Invalid input! Please enter a numeric value.")


def get_two_numbers():
    """
    Prompt the user to enter two numbers sequentially.

    Uses :func:`get_number` to ensure each value is valid before
    returning.

    Returns:
        tuple[float, float]: A tuple ``(a, b)`` of the two numbers
        entered by the user.
    """
    a = get_number("  Enter first number  : ")
    b = get_number("  Enter second number : ")
    return a, b


def addition():
    """
    Perform addition on two user-supplied numbers and print the result.

    Reads two numbers via :func:`get_two_numbers`, computes ``a + b``,
    and displays the equation with the result.
    """
    a, b = get_two_numbers()
    result = a + b
    print(f"\n  ✅  {a} + {b} = {result}")


def subtraction():
    """
    Perform subtraction on two user-supplied numbers and print the result.

    Reads two numbers via :func:`get_two_numbers`, computes ``a - b``,
    and displays the equation with the result.
    """
    a, b = get_two_numbers()
    result = a - b
    print(f"\n  ✅  {a} - {b} = {result}")


def multiplication():
    """
    Perform multiplication on two user-supplied numbers and print the result.

    Reads two numbers via :func:`get_two_numbers`, computes ``a * b``,
    and displays the equation with the result.
    """
    a, b = get_two_numbers()
    result = a * b
    print(f"\n  ✅  {a} × {b} = {result}")


def division():
    """
    Perform division on two user-supplied numbers and print the result.

    Reads two numbers via :func:`get_two_numbers`, then computes ``a / b``.
    If ``b`` is zero, an error message is shown and the function returns
    early without computing a result.

    Guards:
        - Division by zero is rejected with an error message.
    """
    a, b = get_two_numbers()
    if b == 0:
        # Prevent ZeroDivisionError; inform the user and return early
        print("\n  ❌  Error: Division by zero is not allowed!")
        return
    result = a / b
    print(f"\n  ✅  {a} ÷ {b} = {result}")


def modulus():
    """
    Compute the modulus (remainder) of two user-supplied numbers and print it.

    Reads two numbers via :func:`get_two_numbers`, then computes ``a % b``.
    If ``b`` is zero, an error message is shown and the function returns
    early without computing a result.

    Guards:
        - Modulus by zero is rejected with an error message.
    """
    a, b = get_two_numbers()
    if b == 0:
        # Prevent ZeroDivisionError for modulus; inform the user and return
        print("\n  ❌  Error: Modulus by zero is not allowed!")
        return
    result = a % b
    print(f"\n  ✅  {a} % {b} = {result}")


def exponentiation():
    """
    Raise a base number to a given exponent and print the result.

    Reads two numbers via :func:`get_two_numbers`, computes ``a ** b``
    (i.e. a raised to the power of b), and displays the result.
    """
    a, b = get_two_numbers()
    result = a ** b
    print(f"\n  ✅  {a} ^ {b} = {result}")


def square_root():
    """
    Compute the square root of a single user-supplied number and print it.

    Reads one number via :func:`get_number`, then computes ``math.sqrt(a)``.
    If ``a`` is negative, an error message is shown and the function returns
    early because square roots of negative numbers are not real numbers.

    Guards:
        - Negative input is rejected with an informative error message.
    """
    a = get_number("  Enter number : ")
    if a < 0:
        # math.sqrt raises ValueError for negatives; catch it early with a clear message
        print("\n  ❌  Error: Square root of a negative number is not defined in real numbers!")
        return
    result = math.sqrt(a)
    print(f"\n  ✅  √{a} = {result}")


def main():
    """
    Entry point for the Python Calculator application.

    Displays a welcome message and enters an interactive loop that:
      1. Shows the operation menu via :func:`display_menu`.
      2. Reads the user's choice.
      3. Dispatches to the corresponding operation function.
      4. Repeats until the user selects option 8 (Exit).

    The operation-to-function mapping is stored in a dictionary so that
    new operations can be added without modifying the loop logic.
    """
    print("\n  Welcome to the Python Calculator!")

    # Map menu choices (as strings) to their corresponding operation functions
    operations = {
        "1": addition,
        "2": subtraction,
        "3": multiplication,
        "4": division,
        "5": modulus,
        "6": exponentiation,
        "7": square_root,
    }

    # --- Main loop: continues until the user chooses to exit (option 8) ---
    while True:
        display_menu()                                        # Show the menu each iteration
        choice = input("  Select an option (1-8): ").strip() # Read and trim whitespace

        if choice == "8":
            # User chose to exit; print a goodbye message and break the loop
            print("\n  👋  Thank you for using Python Calculator. Goodbye!\n")
            break
        elif choice in operations:
            # Valid choice: call the matching operation function
            operations[choice]()
        else:
            # Unknown choice: inform the user and re-display the menu
            print("\n  ⚠️  Invalid choice! Please select a valid option (1-8).")


# Standard Python entry-point guard: only run main() when executed directly,
# not when this module is imported by another script.
if __name__ == "__main__":
    main()
