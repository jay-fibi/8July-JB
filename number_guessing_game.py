"""
Number Guessing Game
--------------------
A fun interactive number guessing game where the player tries
to guess a randomly generated number within a limited number of attempts.

Features:
  - Random number generation
  - Difficulty levels (Easy, Medium, Hard)
  - Score tracking
  - Play again option
"""

import random


def get_difficulty():
    """Prompt the player to select a difficulty level."""
    print("\n🎮  Select Difficulty Level:")
    print("  1. Easy   (1–50,  10 attempts)")
    print("  2. Medium (1–100,  7 attempts)")
    print("  3. Hard   (1–200,  5 attempts)")

    levels = {
        "1": ("Easy",   50,  10),
        "2": ("Medium", 100,  7),
        "3": ("Hard",   200,  5),
    }

    while True:
        choice = input("Enter choice (1/2/3): ").strip()
        if choice in levels:
            return levels[choice]
        print("❌  Invalid choice. Please enter 1, 2, or 3.")


def play_round():
    """Run a single round of the guessing game. Returns True if the player won."""
    name, upper, max_attempts = get_difficulty()
    secret = random.randint(1, upper)
    attempts_left = max_attempts

    print(f"\n🔢  Guess the number between 1 and {upper}.")
    print(f"     You have {max_attempts} attempt(s). Good luck!\n")

    while attempts_left > 0:
        attempts_left -= 1

        try:
            guess = int(input(f"  [Attempts left: {attempts_left + 1}] Your guess: "))
        except ValueError:
            print("  ⚠️  Please enter a valid integer.")
            attempts_left += 1          # don't count invalid input
            continue

        if guess < 1 or guess > upper:
            print(f"  ⚠️  Out of range! Guess between 1 and {upper}.")
            attempts_left += 1
            continue

        if guess == secret:
            used = max_attempts - attempts_left
            print(f"\n  🎉  Correct! The number was {secret}.")
            print(f"  ✅  You guessed it in {used} attempt(s)!")
            return True
        elif guess < secret:
            print("  📈  Too low! Try a higher number.")
        else:
            print("  📉  Too high! Try a lower number.")

    print(f"\n  😞  Out of attempts! The secret number was {secret}.")
    return False


def main():
    """Main entry point – handles score tracking and replay."""
    print("=" * 45)
    print("     🎯  Welcome to the Number Guessing Game!")
    print("=" * 45)

    wins = 0
    losses = 0

    while True:
        won = play_round()
        if won:
            wins += 1
        else:
            losses += 1

        print(f"\n📊  Score → Wins: {wins}  |  Losses: {losses}")

        again = input("\n🔄  Play again? (yes/no): ").strip().lower()
        if again not in ("yes", "y"):
            break

    print("\n👋  Thanks for playing! Final score:")
    print(f"    Wins: {wins}  |  Losses: {losses}")
    print("=" * 45)


if __name__ == "__main__":
    main()
