import sbt._

class ViperVMProject(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins {

  /* Repositories */
  val javanet = "Java Net Repository" at "http://download.java.net/maven/2/"
  val scala_tools_snapshots = "Scala-Tools Snapshots" at "http://scala-tools.org/repo-snapshots"

  /* Runtime libs */
  val jna = "net.java.dev.jna" % "jna" % "3.2.7"

  /* GUI libs */
  val scala_swing = "org.scala-lang" % "scala-swing" % "2.9.0"

  /* Continuation plugin */
  val continuations = compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.0")
  override def compileOptions = super.compileOptions ++ compileOptions("-P:continuations:enable") ++ Seq(Unchecked)

  /* Logging (SLF4J through grizzled) */
  val grizzled = "org.clapper" %% "grizzled-slf4j" % "0.5"

  /* Logging configuration :
   * - Simple: Output logs to System.err
   * - NOP: Discard logging 
   * - log4j 
   */
  val slf4jsimple = "org.slf4j" % "slf4j-simple" % "1.6.1"
  //val slf4jnop = "org.slf4j" % "slf4j-nop" % "1.6.1"
  //val slf4jlog4j = "org.slf4j" % "slf4j-log4j12" % "1.6.1"

  /* Others */
  val scalaz = "org.scalaz" %% "scalaz-core" % "6.0-SNAPSHOT"

  /* Test libs */
  val scalatest = "org.scalatest" %% "scalatest" % "1.4.1"
  val junit = "junit" % "junit" % "4.8.2"

  /* Runs */
  override def mainClass = None
  
  val run_demo_codegen = runTask(Some("demos.codegen.Main"), testClasspath) dependsOn(testCompile) describedAs "C code generation demo"

  val run_profiler = runTask(Some("org.vipervm.apps.Profiler"), testClasspath) dependsOn(compile) describedAs "Profiling GUI"
  val run_info = runTask(Some("org.vipervm.apps.Info"), testClasspath) dependsOn(compile) describedAs "Platform information"
}

