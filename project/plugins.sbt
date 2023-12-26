// only add this line if you're living on the edge and using
// a version that has "SNAPSHOT" in it
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin(
  "com.indoorvivants" % "bindgen-sbt-plugin" % "0.0.20"
)
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.15")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
addSbtPlugin("com.indoorvivants.vcpkg" % "sbt-vcpkg-native" % "0.0.18")
