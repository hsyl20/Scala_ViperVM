import sbt._

class AuratuneProject(info: ProjectInfo) extends DefaultProject(info) {

   /* Repositories */
   val javanet = "Java Net Repository" at "http://download.java.net/maven/2/"

   /* Runtime libs */
   val jna = "net.java.dev.jna" % "jna" % "3.2.5"

   /* Test libs */
   val scalatest = "org.scalatest" % "scalatest" % "1.2"
   val junit = "junit" % "junit" % "4.8.2"
}

