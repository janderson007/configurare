scalaVersion:="2.13.1"

name := "configurare"
version := "0.1.0-SNAPSHOT"

lazy val global = project
  .in(file("."))
  .aggregate(
    core,
    `example-service`,
    `typesafe-shim`
  )

lazy val `typesafe-shim` = project
  .settings(
    name:= "typesafe-shim",
    scalaVersion:="2.13.1",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.4.0"
    )
  )

lazy val `example-service` = project
  .settings(
    name := "example-service",
    scalaVersion:="2.13.1",
    Test / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "etc" / "service",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"   % "10.1.11",
      "com.typesafe.akka" %% "akka-stream" % "2.5.26"
    ),
    javaOptions ++= Seq(
      "-Dconfigurare.root=src/main/etc/service"
    ),
    fork := true
    //Runtime / fork := true
  ).dependsOn(
    core,
   `typesafe-shim`
  )

lazy val core = project
  .settings(
    name := "core",
    scalaVersion:="2.13.1",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % "2.13.1",
      "org.scala-lang" % "scala-reflect" % "2.13.1",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.softwaremill.quicklens" %% "quicklens" % "1.5.0"
    )
  )

