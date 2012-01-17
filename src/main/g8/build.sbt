name := ""

organization := "cc.acs"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies +=  "org.scalaz" %% "scalaz-core" % "6.0.3"

initialCommands in console := """
  import scalaz._
  import Scalaz._
  import acs.boxes.Boxes._
  println("Commence hacking...")
"""

// set Ivy logging to be at the highest level
ivyLoggingLevel := UpdateLogging.Full

// set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

// set the prompt (for the current project) to include the username
shellPrompt := { state => System.getProperty("user.name") + "> " }

// disable printing timing information, but still print [success]
showTiming := false

// disable using the Scala version in output paths and artifacts
// crossPaths := false

// fork a new JVM for 'run' and 'test:run'
// fork := true

// add a JVM option to use when forking a JVM for 'run'
// javaOptions += "-Xmx2G"

// only use a single thread for building
parallelExecution := true

// only show warnings and errors on the screen for all tasks (the default is Info)
logLevel := Level.Info

// only store messages at info and above (the default is Debug)
//   this is the logging level for replaying logging with 'last'
persistLogLevel := Level.Debug

// only show 10 lines of stack traces
// traceLevel := 10

// only show stack traces up to the first sbt stack frame
traceLevel := 0

publishTo <<= (version) {version: String => {
  def repo(name: String) = name at "http://iesl.cs.umass.edu:8081/nexus/content/repositories/" + name
  val isSnapshot = version.trim.endsWith("SNAPSHOT")
  val repoName = if (isSnapshot) "snapshots" else "releases"
  Some(repo(repoName)) }}

credentials += {
  Seq("build.publish.user", "build.publish.password").map(k => Option(System.getProperty(k))) match {
    case Seq(Some(user), Some(pass)) => Credentials("Sonatype Nexus Repository Manager", "iesl.cs.umass.edu", user, pass)
    case _ => Credentials(Path.userHome / ".ivy2" / ".credentials")
  }}

// publishTo := Some("snapshots" at "http://iesl.cs.umass.edu:8081/nexus/content/repositories/snapshots")
//credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

seq(lsSettings :_*)
