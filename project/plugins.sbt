// only add this line if you're living on the edge and using
// a version that has "SNAPSHOT" in it
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin(
  "com.indoorvivants" % "bindgen-sbt-plugin" % "0.0.14"
)
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.10")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
addSbtPlugin(
  "com.indoorvivants.vcpkg" % "sbt-vcpkg" % "0.0.7+8-2c32f59d-SNAPSHOT"
)
