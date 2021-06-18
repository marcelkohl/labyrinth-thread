[![Maintenance](https://img.shields.io/badge/Maintained%3F-no-red.svg)](https://bitbucket.org/lbesson/ansi-colors)
[![Generic badge](https://img.shields.io/badge/Status-Deprecated-orange.svg)](https://shields.io/)
[![GPLv3 license](https://img.shields.io/badge/License-GPLv3-blue.svg)](http://perso.crans.org/besson/LICENSE.html)
[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://GitHub.com/Naereen/ama)

# labyrinth-thread
Java project making use of threads

Making use of threads this small project draws a random labyrinth and allows multiple independent characters to find the path to the final point;

# Layrinth
The labyrinth is randomly generated on demand and it is based on the [Randomized depth-first search](https://en.wikipedia.org/wiki/Maze_generation_algorithm).

# Characters moves
Instead of a random move on every iteration, I've implemented a kind of memory to track the paths were it already passed, so the decision made is based on the less traveled path first;

# Graphics
The sprites that compose the challenge can be customized as needed.
