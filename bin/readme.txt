please add lib/jgrapht-0.8.2/jrapht-jdk1.6.jar to build path. It includes JgraphT which will be needed for graph algorithms.

Right now the player assumes full view and there exist a path to exit at the same time.

Two maps:
1. Once you went one step wrong, can't go back. e.g. go down at the first step.
2. Big map with a small 3*3 map (exit is in the center). Have to be careful not to step on the exit in the 3*3 map.