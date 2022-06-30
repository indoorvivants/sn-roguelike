import com.indoorvivants.vcpkg.Platform
Global / onChangedBuildSource := ReloadOnSourceChanges

import bindgen.interface.Binding

lazy val parts = project
  .in(file("."))
  .aggregate(
    part1,
    part2,
    part3
  )
  .settings(common)

lazy val part1 = project
  .in(file("part1"))
  .enablePlugins(ScalaNativePlugin)
  .settings(common)
  .configure(addRaylib)

lazy val part2 = project
  .in(file("part2"))
  .enablePlugins(ScalaNativePlugin)
  .settings(common)
  .configure(addRaylib)

lazy val part3 = project
  .in(file("part3"))
  .enablePlugins(ScalaNativePlugin)
  .settings(common)
  .configure(addRaylib)

// common settings

val common = Seq(
  scalaVersion := "3.1.3"
)

def addRaylib(proj: Project): Project =
  proj
    .enablePlugins(VcpkgPlugin, BindgenPlugin)
    .settings(
      vcpkgDependencies := Set("raylib"),
      nativeConfig := {
        val conf = nativeConfig.value

        val pkgConfig = vcpkgConfigurator.value

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
      },
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
