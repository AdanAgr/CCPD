#!/usr/bin/env python

class Philosopher:
    def __init__(self, name):
        self.name = name
        self.isEating = False

    def eat(self, left_fork, right_fork):
        if not left_fork.isTaken and not right_fork.isTaken:
            left_fork.isTaken = True
            right_fork.isTaken = True
            self.isEating = True
            print(f"{self.name} starts eating.")
        else:
            print(f"{self.name} can't eat because forks are not available.")

    def think(self, left_fork, right_fork):
        if self.isEating:
            left_fork.isTaken = False
            right_fork.isTaken = False
            self.isEating = False
            print(f"{self.name} starts thinking.")

class Fork:
    def __init__(self):
        self.isTaken = False

def main():
    num_philosophers = 5
    philosophers = [Philosopher(f"Philosopher {i}") for i in range(num_philosophers)]
    forks = [Fork() for _ in range(num_philosophers)]

    for turn in range(10):
        philosopher = philosophers[turn % num_philosophers]
        left_fork = forks[turn % num_philosophers]
        right_fork = forks[(turn + 1) % num_philosophers]
        
        philosopher.eat(left_fork, right_fork)
        philosopher.think(left_fork, right_fork)

if __name__ == "__main__":
    main()
