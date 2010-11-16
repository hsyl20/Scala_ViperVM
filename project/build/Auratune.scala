import sbt._

class AuratuneProject(info: ProjectInfo) extends DefaultProject(info) {
   val javanet = "Java Net Repository" at "http://download.java.net/maven/2/"

   val jna = "net.java.dev.jna" % "jna" % "3.2.5"
}

