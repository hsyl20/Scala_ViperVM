import sbt._
import Keys._

import com.github.retronym.SbtOneJar
import sbtassembly.Plugin._
import AssemblyKeys._

object MyProject extends Build {
  val buildOrganization = "org.vipervm"
  val buildName         = "ViperVM"
  val buildVersion      = "0.1-SNAPSHOT"
  val buildScalaVersion = "2.9.1"

  lazy val project = Project (buildName, file("."), settings = mySettings)

  val replTaskKey = TaskKey[Unit]("run-repl", "Read-Eval-Print-Loop")

  val mySettings = Defaults.defaultSettings ++ SbtOneJar.oneJarSettings ++ assemblySettings ++ Seq (
    organization := buildOrganization,
    name         := buildName,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions := Seq("-deprecation", "-unchecked"),
    resolvers    := myResolvers,
    libraryDependencies := myDependencies,

    /* Do not interfer with OpenCL signals */
    fork := true,
    javaOptions += "-Xrs",

    /* GIT ready shell prompt */
    shellPrompt  := ShellPrompt.buildShellPrompt,

    /* Continuation plugin */
    autoCompilerPlugins := true,
    addCompilerPlugin("org.scala-lang.plugins" % "continuations" % buildScalaVersion),
    scalacOptions += "-P:continuations:enable",

    /* Tasks */
    fullRunTask(TaskKey[Unit]("run-profiler", "Profiler GUI"), Test, "org.vipervm.apps.Profiler"),
    fullRunTask(TaskKey[Unit]("run-platform-info", "Platform information"), Test, "org.vipervm.apps.Info"),
    fullRunTask(TaskKey[Unit]("run-sample", "Sample application"), Test, "org.vipervm.apps.Sample"),
    fullRunTask(replTaskKey, Test, "org.vipervm.fp.REPL"),
    fork in replTaskKey := true,
    outputStrategy in replTaskKey := Some(StdoutOutput),
    connectInput in replTaskKey := true,

    /* One-jar options */
    mainClass in SbtOneJar.oneJar := Some("org.vipervm.apps.Sample"),

    /* Assembly options */
    mainClass in assembly := Some("org.vipervm.apps.Sample"),
    test in assembly := {}
  )

  val myResolvers = Seq(
    "Sun Maven2 Repo" at "http://download.java.net/maven/2",
    "Oracle Maven2 Repo" at "http://download.oracle.com/maven",
    "Scala-Tools Snapshots" at "http://scala-tools.org/repo-snapshots",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  )

  val myDependencies = Seq (
    "org.scala-lang" % "jline" % buildScalaVersion,
    "jline" % "jline" % "1.0",
    "org.scala-lang" % "scala-swing" % buildScalaVersion,
    "net.java.dev.jna" % "jna" % "3.4.0",
    "commons-io" % "commons-io" % "1.4",
    "org.clapper" %% "grizzled-slf4j" % "0.6.6",
    "org.scalaz" %% "scalaz-full" % "6.0.3",
    "org.scalatest" %% "scalatest" % "1.6.1",
    "batik" % "batik-svggen" % "1.6-1",
    "batik" % "batik-swing" % "1.6-1",
    "batik" % "batik-css" % "1.6-1",
    "com.typesafe.akka" % "akka-actor" % "2.0-RC2",
    /* Logging configuration :
     * - Simple: Output logs to System.err
     * - NOP: Discard logging 
     * - log4j 
     */
  "org.slf4j" % "slf4j-simple" % "1.6.2"
  //  "org.slf4j" % "slf4j-nop" % "1.6.2"
  //"org.slf4j" % "slf4j-log4j12" % "1.6.2"
  )

}

// Shell prompt which show the current project, 
// git branch and build version
object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  
  val current = """\*\s+([\w-]+)""".r
  
  def gitBranches = ("git branch --no-color" lines_! devnull mkString)
  
  val buildShellPrompt = { 
    (state: State) => {
      val currBranch = 
        current findFirstMatchIn gitBranches map (_ group(1)) getOrElse "-"
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (
        currProject, currBranch, MyProject.buildVersion
      )
    }
  }
}
