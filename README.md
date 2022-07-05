# sn-roguelike

Look ma, no prior game development experience!

Doing Roguelike tutorial, even though I haven't really played one and never 
worked on a game before.

We're using my Scala Native, [binding generator](https://sn-bindgen.indoorvivants.com), Scala 3,
and the great [Raylib](https://www.raylib.com).

1. Set up everything for Scala Native to work: https://scala-native.readthedocs.io/en/latest/user/setup.html

2. This example was written on an Apple machine, and thus the clang flags were hardcoded, according to instructions: https://github.com/raysan5/raylib/wiki/Working-on-macOS. Change them to the appropriate flags for your platform.

I use vcpkg, so _theoretically_ it should _just work_

Parts completed:


- [x] Part 1 `sbt part1/run`
- [x] Part 2 `sbt part2/run`
- [x] Part 3 `sbt part3/run`
- [x] Part 4 `sbt part4/run`
