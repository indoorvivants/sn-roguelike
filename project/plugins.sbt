// only add this line if you're living on the edge and using 
// a version that has "SNAPSHOT" in it
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin("com.indoorvivants" % "bindgen-sbt-plugin" % "0.0.10+3-e5a1879b-SNAPSHOT")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.5")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")
addSbtPlugin("com.indoorvivants.vcpkg" % "sbt-vcpkg" % "0.0.5")
