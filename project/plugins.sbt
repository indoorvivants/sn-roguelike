// only add this line if you're living on the edge and using
// a version that has "SNAPSHOT" in it
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin(
  "com.indoorvivants" % "bindgen-sbt-plugin" % "0.0.24"
)
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.17")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.5")
addSbtPlugin("com.indoorvivants.vcpkg" % "sbt-vcpkg-native" % "0.0.19")
