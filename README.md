[![Maintenance](https://img.shields.io/badge/Maintained%3F-no-red.svg)](#)
[![Generic badge](https://img.shields.io/badge/Status-Deprecated-orange.svg)](#)
[![GPLv3 license](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)
[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/marcelkohl)

# labyrinth-thread
Java project making use of threads

![Screenshot 01](https://github.com/marcelkohl/labyrinth-thread/blob/main/demo.png?raw=true)

Making use of threads this small project draws a random labyrinth and allows multiple independent characters to find the path to the final point;

# Layrinth
The labyrinth is randomly generated on demand and it is based on the [Randomized depth-first search](https://en.wikipedia.org/wiki/Maze_generation_algorithm).

# Characters moves
Instead of a random move on every iteration, I've implemented a kind of memory to track the paths were it already passed, so the decision made is based on the less traveled path first;

# Graphics
The sprites that compose the project can be customized as needed.

![Screenshot 02](https://github.com/marcelkohl/labyrinth-thread/blob/main/demo2.png?raw=true)

# Build and running
I recommend to use [NetBeans](https://netbeans.apache.org/download/nb124/nb124.html) to build and run the project but you can use your preferred tool. Just open the project folder and run.
