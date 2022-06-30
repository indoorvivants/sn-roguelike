Global / onChangedBuildSource := ReloadOnSourceChanges

import bindgen.interface.Binding

lazy val parts = project
  .in(file("."))
  .aggregate(
    part1,
    part2,
    part3
  )

lazy val part1 = project
  .in(file("part1"))
  .enablePlugins(ScalaNativePlugin)
  .dependsOn(raylib)
  .settings(common)

lazy val part2 = project
  .in(file("part2"))
  .enablePlugins(ScalaNativePlugin)
  .dependsOn(raylib)
  .settings(common)

lazy val part3 = project
  .in(file("part3"))
  .enablePlugins(ScalaNativePlugin)
  .dependsOn(raylib)
  .settings(common)

lazy val raylib =
  project
    .in(file(".raylib"))
    .enablePlugins(ScalaNativePlugin, BindgenPlugin)
    .settings(common)
    .settings(
      bindgenBindings := {
        val base = (ThisBuild / baseDirectory).value
        Seq(
          Binding(
            base / "raylib" / "src" / "raylib.h",
            "raylib",
            cImports = List("raylib.h")
          )
        )
      }
    )

// common settings

val common = Seq(
  scalaVersion := "3.1.3",
  nativeConfig := {
    val base = (ThisBuild / baseDirectory).value
    val conf = nativeConfig.value
    val staticLib = base / "raylib" / "src" / "libraylib.a"
    val include = base / "raylib" / "src"

    val extras =
      "-framework CoreVideo -framework IOKit -framework Cocoa -framework GLUT -framework OpenGL"
        .split(" ")
        .toList

    conf
      .withLinkingOptions(
        conf.linkingOptions ++ List(staticLib.toString) ++ extras
      )
      .withCompileOptions(conf.compileOptions ++ List(s"-I$include") ++ extras)
  }
)
