import com.indoorvivants.vcpkg.Platform
Global / onChangedBuildSource := ReloadOnSourceChanges

import bindgen.interface.Binding

lazy val parts = project
  .in(file("."))
  .aggregate(
    part1,
    part2,
    part3,
    part4
  )
  .settings(common)

lazy val part1 = project
  .in(file("part1"))
  .enablePlugins(ScalaNativePlugin)
  .settings(common)
  .settings(addRaylib)
  .dependsOn(bindings)

lazy val part2 = project
  .in(file("part2"))
  .enablePlugins(ScalaNativePlugin)
  .settings(common)
  .settings(addRaylib)
  .dependsOn(bindings)

lazy val part3 = project
  .in(file("part3"))
  .enablePlugins(ScalaNativePlugin)
  .settings(common)
  .settings(addRaylib)
  .dependsOn(bindings)

lazy val part4 = project
  .in(file("part4"))
  .enablePlugins(ScalaNativePlugin)
  .settings(common)
  .settings(addRaylib)
  .dependsOn(bindings)

lazy val bindings = project
  .in(file(".bindings"))
  .settings(common)
  .enablePlugins(VcpkgPlugin, BindgenPlugin)
  .settings(
    vcpkgDependencies := Set("raylib"),
    bindgenBindings := {
      Seq(
        Binding(
          vcpkgManager.value.includes("raylib") / "raylib.h",
          "raylib",
          cImports = List("raylib.h")
        )
      )
    }
  )

// common settings

val common = Seq(
  scalaVersion := "3.1.3",
  libraryDependencies += "com.outr" %%% "scribe" % "3.10.0"
)

val addRaylib = Seq(
  nativeConfig := {
    val conf = nativeConfig.value

    val pkgConfig = (bindings / vcpkgConfigurator).value

    val platformSpecific = Seq(
      "-framework",
      "CoreVideo",
      "-framework",
      "GLUT",
      "-framework",
      "OpenGL"
    )

    val compilation = pkgConfig.compilationFlags("raylib")
    val linking = pkgConfig.linkingFlags("raylib")

    conf
      .withLinkingOptions(
        conf.linkingOptions ++ linking ++ platformSpecific
      )
      .withCompileOptions(conf.compileOptions ++ compilation)
  }
)
