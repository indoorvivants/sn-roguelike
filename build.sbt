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
    vcpkgDependencies := VcpkgDependencies("raylib"),
    bindgenBindings := {
      Seq(
        Binding
          .builder(
            vcpkgConfigurator.value.includes("raylib") / "raylib.h",
            "raylib"
          )
          .addCImport("raylib.h")
          .build
      )
    }
  )

// common settings

val common = Seq(
  scalaVersion := "3.2.2",
  libraryDependencies += "com.outr" %%% "scribe" % "3.11.5",
  resolvers += Resolver.sonatypeRepo("snapshots")
)

val addRaylib = Seq(
  nativeConfig := {
    val conf = nativeConfig.value

    val pkgConfig = (bindings / vcpkgConfigurator).value.pkgConfig

    val platformSpecific = Seq(
      "-framework",
      "CoreVideo",
      "-framework",
      "GLUT",
      "-framework",
      "OpenGL"
    )

    val compilation =
      pkgConfig.updateCompilationFlags(conf.compileOptions, "raylib")
    val linking = pkgConfig.updateLinkingFlags(conf.linkingOptions, "raylib")

    conf
      .withLinkingOptions(
        linking ++ platformSpecific
      )
      .withCompileOptions(compilation)
  }
)
