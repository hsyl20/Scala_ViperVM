import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "org.vipervm"
  val buildName         = "ViperVM"
  val buildVersion      = "0.1"
  val buildScalaVersion = "2.9.1"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    name         := buildName,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt  := ShellPrompt.buildShellPrompt
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
        currProject, currBranch, BuildSettings.buildVersion
      )
    }
  }
}

object Resolvers {
  val sunrepo    = "Sun Maven2 Repo" at "http://download.java.net/maven/2"
  val oraclerepo = "Oracle Maven2 Repo" at "http://download.oracle.com/maven"

  val scala_tools_snapshots = "Scala-Tools Snapshots" at "http://scala-tools.org/repo-snapshots"

  val  bridj = "nativelibs4java" at "http://nativelibs4java.sourceforge.net/maven/nativelibs4java"

  val allResolvers = Seq(sunrepo, oraclerepo, scala_tools_snapshots)
}

object Dependencies {
  import BuildSettings._

  /* JLine for the REPL */
  val jline = "org.scala-lang" % "jline" % buildScalaVersion

  /* Runtime libs */
  val jna = "net.java.dev.jna" % "jna" % "3.3.0"

  /* GUI libs */
  val scala_swing = "org.scala-lang" % "scala-swing" % buildScalaVersion

  /* Logging (SLF4J through grizzled) */
  val grizzled = "org.clapper" %% "grizzled-slf4j" % "0.6.6"

  /* Logging configuration :
   * - Simple: Output logs to System.err
   * - NOP: Discard logging 
   * - log4j 
   */
  val slf4jsimple = "org.slf4j" % "slf4j-simple" % "1.6.2"
  //val slf4jnop = "org.slf4j" % "slf4j-nop" % "1.6.2"
  //val slf4jlog4j = "org.slf4j" % "slf4j-log4j12" % "1.6.2"

  /* Others */
  val scalaz = "org.scalaz" %% "scalaz-full" % "6.0.3"

  val bridj = "com.nativelibs4java" % "bridj" % "0.5"

  /* Test libs */
  val scalatest = "org.scalatest" %% "scalatest" % "1.6.1"
  val junit = "junit" % "junit" % "4.8.2"
}


object Tasks {
  val demoCodegenTask = TaskKey[Unit]("run-demo-codegen", "C Code generation demo")
  val profilerTask = TaskKey[Unit]("run-profiler", "Profiler GUI")
  val infoTask = TaskKey[Unit]("run-platform-info", "Platform information")

  fork in profilerTask := true

  val all = Seq(
    fullRunTask(demoCodegenTask, Test, "demos.codegen.Main"),
    fullRunTask(profilerTask, Test, "org.vipervm.apps.Profiler"),
    fullRunTask(infoTask, Test, "org.vipervm.apps.Info")
  )

}

object ViperVMBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  // Sub-project specific dependencies
  val commonDeps = Seq (
    jline,
    jna,
    scala_swing,
    grizzled,
    slf4jsimple,
    scalaz,
    scalatest,
    junit
  )

  lazy val vipervm = Project (
    "vipervm",
    file ("."),
    settings = buildSettings ++ Seq (scalacOptions := Seq("-deprecation", "-unchecked"),
                                     resolvers := allResolvers,
                                     libraryDependencies := commonDeps)
                             ++ Tasks.all
  )

}

