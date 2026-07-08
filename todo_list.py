"""
To-Do List Manager
-------------------
A command-line to-do list application that lets you manage
your daily tasks with persistent storage in a JSON file.

Features:
  - Add tasks with optional priority (High / Medium / Low)
  - View all tasks (filter by status or priority)
  - Mark tasks as complete
  - Delete tasks
  - Persistent storage via JSON
  - Task statistics summary
"""

import json
import os
from datetime import datetime

DATA_FILE = "todo_data.json"


def load_tasks() -> list:
    """Load tasks from the JSON data file. Returns an empty list if none exist."""
    if os.path.exists(DATA_FILE):
        with open(DATA_FILE, "r") as f:
            return json.load(f)
    return []


def save_tasks(tasks: list) -> None:
    """Persist the task list to the JSON data file."""
    with open(DATA_FILE, "w") as f:
        json.dump(tasks, f, indent=4)


def next_id(tasks: list) -> int:
    """Return the next available task ID."""
    return max((t["id"] for t in tasks), default=0) + 1


def add_task(tasks: list) -> None:
    """Prompt the user for a new task and append it to the list."""
    title = input("  Task title: ").strip()
    if not title:
        print("  Task title cannot be empty.")
        return

    print("  Priority: 1) High  2) Medium  3) Low")
    p_map = {"1": "High", "2": "Medium", "3": "Low"}
    p_choice = input("  Choose priority (1/2/3) [default=2]: ").strip() or "2"
    priority = p_map.get(p_choice, "Medium")

    task = {
        "id": next_id(tasks),
        "title": title,
        "priority": priority,
        "status": "Pending",
        "created_at": datetime.now().strftime("%Y-%m-%d %H:%M"),
        "completed_at": None,
    }
    tasks.append(task)
    save_tasks(tasks)
    print(f"  Task #{task['id']} added successfully!")


def view_tasks(tasks: list) -> None:
    """Display all tasks with optional filtering."""
    if not tasks:
        print("  No tasks found.")
        return

    print("  Filter: 1) All  2) Pending  3) Completed")
    f_choice = input("  Choose filter (1/2/3) [default=1]: ").strip() or "1"
    f_map = {"1": None, "2": "Pending", "3": "Completed"}
    status_filter = f_map.get(f_choice)

    filtered = [t for t in tasks if status_filter is None or t["status"] == status_filter]

    if not filtered:
        print("  No tasks match the selected filter.")
        return

    priority_order = {"High": 0, "Medium": 1, "Low": 2}
    filtered.sort(key=lambda t: (t["status"] != "Pending", priority_order.get(t["priority"], 9)))

    print(f"\n  {'ID':<5} {'Title':<30} {'Priority':<10} {'Status':<12} {'Created'}")
    print("  " + "-" * 75)
    for t in filtered:
        status_icon = "[Done]" if t["status"] == "Completed" else "[Pending]"
        print(
            f"  {t['id']:<5} {t['title'][:28]:<30} "
            f"{t['priority']:<10} "
            f"{status_icon:<12} "
            f"{t['created_at']}"
        )
    print()


def complete_task(tasks: list) -> None:
    """Mark a task as completed."""
    view_tasks(tasks)
    try:
        tid = int(input("  Enter Task ID to mark as complete: ").strip())
    except ValueError:
        print("  Invalid ID.")
        return

    for task in tasks:
        if task["id"] == tid:
            if task["status"] == "Completed":
                print(f"  Task #{tid} is already completed.")
            else:
                task["status"] = "Completed"
                task["completed_at"] = datetime.now().strftime("%Y-%m-%d %H:%M")
                save_tasks(tasks)
                print(f"  Task #{tid} marked as completed!")
            return

    print(f"  Task #{tid} not found.")


def delete_task(tasks: list) -> None:
    """Delete a task by ID."""
    view_tasks(tasks)
    try:
        tid = int(input("  Enter Task ID to delete: ").strip())
    except ValueError:
        print("  Invalid ID.")
        return

    for i, task in enumerate(tasks):
        if task["id"] == tid:
            removed = tasks.pop(i)
            save_tasks(tasks)
            print(f"  Task #{tid} '{removed['title']}' deleted.")
            return

    print(f"  Task #{tid} not found.")


def show_stats(tasks: list) -> None:
    """Print a summary of task statistics."""
    total = len(tasks)
    completed = sum(1 for t in tasks if t["status"] == "Completed")
    pending = total - completed
    high = sum(1 for t in tasks if t["priority"] == "High" and t["status"] == "Pending")

    print(f"\n  Task Statistics:")
    print(f"    Total tasks   : {total}")
    print(f"    Completed     : {completed}")
    print(f"    Pending       : {pending}")
    print(f"    High priority : {high} pending\n")


MENU = """
  ==============================
       To-Do List Manager
  ==============================
    1. Add Task
    2. View Tasks
    3. Mark Task as Complete
    4. Delete Task
    5. Show Statistics
    6. Exit
  ==============================
"""


def main():
    print(MENU)
    tasks = load_tasks()

    actions = {
        "1": add_task,
        "2": view_tasks,
        "3": complete_task,
        "4": delete_task,
        "5": show_stats,
    }

    while True:
        choice = input("  Select an option (1-6): ").strip()
        if choice == "6":
            print("\n  Goodbye! Stay productive!\n")
            break
        elif choice in actions:
            print()
            actions[choice](tasks)
            print()
        else:
            print("  Invalid option. Please choose between 1 and 6.\n")


if __name__ == "__main__":
    main()
