import sbt._

class ViperVMProject(info: ProjectInfo) extends DefaultProject(info) {

  /* Repositories */
  val javanet = "Java Net Repository" at "http://download.java.net/maven/2/"
  val scala_tools_snapshots = "Scala-Tools Snapshots" at "http://scala-tools.org/repo-snapshots"

  /* Runtime libs */
  val jna = "net.java.dev.jna" % "jna" % "3.2.5"

  /* GUI libs */
  val scala_swing = "org.scala-lang" % "scala-swing" % "2.8.1"

  /* Others */
  val scalaz = "org.scalaz" %% "scalaz-core" % "6.0-SNAPSHOT"

  /* Test libs */
  val scalatest = "org.scalatest" % "scalatest" % "1.2"
  val junit = "junit" % "junit" % "4.8.2"

  /* Runs */
  override def mainClass = None
  
  val run_demo_codegen = runTask(Some("demos.codegen.Main"), testClasspath) dependsOn(testCompile) describedAs "C code generation demo"

  val run_profiler = runTask(Some("fr.hsyl20.vipervm.apps.Profiler"), testClasspath) dependsOn(compile) describedAs "Profiling GUI"
  val run_info = runTask(Some("fr.hsyl20.vipervm.apps.Info"), testClasspath) dependsOn(compile) describedAs "Platform information"
}

