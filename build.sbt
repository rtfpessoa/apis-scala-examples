

name := "akka-grpc-scala-example"
version := "1.0.0-SNAPSHOT"

lazy val akkaGrpcScalaExample = (project in file("."))
  .enablePlugins(AkkaGrpcPlugin) // enables source generation for gRPC
  // ALPN agent
  .enablePlugins(JavaAgent)
  .settings(
    akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala),
    akkaGrpcGeneratedSources := Seq(AkkaGrpc.Client, AkkaGrpc.Server)
  )

scalaVersion := "2.12.8"
scalacOptions ++= List("-encoding", "utf8", "-deprecation", "-feature", "-unchecked")

// There is a bug in akka-http 10.1.4 that makes it not work with gRPC+Play,
// so we need to downgrade to 10.1.3 (or move to 10.1.5 when that's out)
// https://github.com/akka/akka-http/issues/2168
dependencyOverrides += "com.typesafe.akka" %% "akka-http-core" % "10.1.5"
dependencyOverrides += "com.typesafe.akka" %% "akka-http" % "10.1.5"

javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test"

libraryDependencies += "com.google.api.grpc" % "proto-google-common-protos" % "1.13.0-pre2"

// Test libraries
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
