// only add this line if you're living on the edge and using
// a version that has "SNAPSHOT" in it
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin(
  "com.indoorvivants" % "bindgen-sbt-plugin" % "0.0.13+8-5b836eb5-SNAPSHOT"
)
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.7")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")
addSbtPlugin("com.indoorvivants.vcpkg" % "sbt-vcpkg" % "0.0.6")
