# Bowling Game

Software Refactoring project based on a Bowling Lane manager and simulator written in Java.
(The original source of this project is unknown, was given as Software Systems assignment in IIIT Hyderabad).

## Description

The app simulates a Bowling lane.
Parties can be added on the queue to bowl, the get scheduled on the lanes.
The state of the lanes, and the bowling pins are shown for every frame.
All this happens parallely on separate threads.

As the starting point of the project, the full working code was available.
Through this project, we have only refactored the code to improve metrics and improve maintainibility.
Play/Pause, Increasing limit of persons per party, and Statisitics Query window were new features implemented by us.

## Compile and Run

Fetch the project and enter the source directory

```bash
git clone github.com/GaurangTandon/extensible-bowling-game
cd extensible-bowling-game/src
```

To run the simulators, please type in the following commands in your terminal

```bash
javac BowlingAlleyDriver.java
java BowlingAlleyDriver
```

## File Organisation

* `src/`: Contains all the source files
* `uml/`: Contains the UML class diagrams
* `doc/ : Contains the files for the report

## Major Refactors

Please read `doc/design.pdf` for a detail on refactors performed through the course of the project.

Major Refactoring principles used through the project are as follows:

* Reduction of Cyclomatic Complexity
* Reduction of Coupling
* Improving Cohesion
* Implementation of Builder and Observer patterns

The entire class structure was overhauled for the same and this serves as a reasonable future guide 
for writing maintainable code.
