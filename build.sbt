name := "guardrail-scala-example"
version := "1.0.0-SNAPSHOT"

/* Available arguments:
    specPath: java.io.File
    pkg: String
    dto: String
    framework: String
    tracing: Boolean
*/
guardrailTasks in Compile := List(
  Server(file("src/main/resources/helloworld.yaml"), pkg = "hello.world", tracing = false),
  Client(file("src/main/resources/helloworld.yaml"), pkg = "hello.world", tracing = false)
)

scalaVersion := "2.12.8"
scalacOptions ++= List("-encoding", "utf8", "-deprecation", "-feature", "-unchecked")
scalacOptions += "-Ypartial-unification"

// Akka HTTP
val akkaHttpVersion = "10.1.7"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.19"

val circeVersion = "0.6.0"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-java8"
).map(_ % circeVersion)

// Test libraries
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.19" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
