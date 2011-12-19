import sbt._
import Keys._
import Scope.{GlobalScope, ThisScope}

import com.github.siasia._
import WebPlugin._
import PluginKeys._


object BuildSettings {
  val buildOrganization = "cc.rexa2"
  val buildScalaVersion = "2.9.1"
  val buildVersion = "0.1-SNAPSHOT"

  val buildSettings = Defaults.defaultSettings ++
  Seq (
    organization := buildOrganization,
    scalaVersion := buildScalaVersion,
    version := buildVersion,
    parallelExecution := true,
    retrieveManaged := true,
    autoCompilerPlugins := true,
    // resolvers += ScalaToolsSnapshots,
    externalResolvers <<= resolvers map { rs =>
      Resolver.withDefaultResolvers(rs)},
    // unmanagedJars in Compile <<= baseDirectory map { base => (base / "lib" ** "*.jar").classpath },
    moduleConfigurations ++= Resolvers.moduleConfigurations,
    javacOptions ++= Seq("-Xlint:unchecked"),
    publishTo := Some(Resolvers.IESLSnapshotRepo),
    publishArtifact in (Compile, packageDoc) := false,
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xcheckinit", "-encoding", "utf8"),
    shellPrompt := ShellPrompt.buildShellPrompt)
}

object ShellPrompt {

  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }

  val current = """\*\s+([^\s]+)""".r

  def gitBranches = ("git branch --no-color" lines_! devnull mkString)
  def hgBranch = ("hg branch" lines_! devnull mkString)

  val buildShellPrompt = {
    (state: State) => {
      val currBranch = hgBranch
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (currBranch, currProject, BuildSettings.buildVersion)
    }
  }

}



object Resolvers {
  val CasbahRepo              = "Casbah Repo" at "http://repo.bumnetworks.com/releases"
  val CasbahRepoReleases      = "Casbah Release Repo" at "http://repo.bumnetworks.com/releases"
  val CasbahSnapshotRepo      = "Casbah Snapshots" at "http://repo.bumnetworks.com/snapshots"
  val CodaHaleRepo            = "Coda Hale's Repository" at "http://maven.mojolly.com/content/repositories/codahale/"
  val CodehausRepo            = "Codehaus Repo" at "http://repository.codehaus.org"
  val FuseSourceReleases      = "FuseSource Snapshot Repository" at "http://repo.fusesource.com/nexus/content/groups/public"
  val FuseSourceSnapshots     = "FuseSource Snapshot Repository" at "http://repo.fusesource.com/nexus/content/repositories/snapshots"
  val FusesourceSnapshotRepo  = "Fusesource Snapshots" at "http://repo.fusesource.com/nexus/content/repositories/snapshots"
  val GuiceyFruitRepo         = "GuiceyFruit Repo" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
  val IESLRepo                = "IESL Repo" at "http://iesl.cs.umass.edu:8081/nexus/content/repositories/releases"
  val IESLSnapshotRepo        = "IESL Snapshot Repo" at "http://iesl.cs.umass.edu:8081/nexus/content/repositories/snapshots"
  val JBossRepo               = "JBoss Repo" at "https://repository.jboss.org/nexus/content/groups/public/"
  val JavaNetRepo             = "java.net Repo" at "http://download.java.net/maven/2"
  val JlineRepo               = "JLine Repo" at "http://jline.sourceforge.net/m2repo"
  val SonatypeSnapshotRepo    = "Sonatype OSS Repo" at "http://oss.sonatype.org/content/repositories/releases"
  val SonatypeSnapsots        = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val SunJDMKRepo             = "Sun JDMK Repo" at "http://wp5.e-taxonomy.eu/cdmlib/mavenrepo"
  val TypesafeRepository      = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  val ZookeeperRepo           = "Zookeeper Repo" at "http://lilycms.org/maven/maven2/deploy/"

  val LocalIvy                = Resolver.file("Local .ivy", Path.userHome / ".ivy2" / "local" asFile)
  val LocalM2                 = "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

  val moduleConfigurations = Seq(
    ModuleConfiguration("se.scalablesolutions.akka",TypesafeRepository),
    ModuleConfiguration("voldemort.store.compress",TypesafeRepository),
    ModuleConfiguration("org.codehaus.aspectwerkz",TypesafeRepository),
    ModuleConfiguration("ch.qos.logback",sbt.DefaultMavenRepository),
    ModuleConfiguration("com.atomikos",sbt.DefaultMavenRepository),
    ModuleConfiguration("com.novus", CasbahRepo),
    ModuleConfiguration("com.novus",CasbahRepoReleases),
    ModuleConfiguration("com.sun.jdmk", SunJDMKRepo),
    ModuleConfiguration("com.sun.jersey", JavaNetRepo),
    ModuleConfiguration("com.sun.jersey.contribs", JavaNetRepo),
    ModuleConfiguration("com.sun.jmx", SunJDMKRepo),
    ModuleConfiguration("com.weiglewilczek.slf4s", ScalaToolsReleases),
    ModuleConfiguration("javax.jms", SunJDMKRepo),
    ModuleConfiguration("jgroups", JBossRepo),
    ModuleConfiguration("net.debasishg", ScalaToolsReleases),
    ModuleConfiguration("org.apache.hadoop.zookeeper",ZookeeperRepo),
    ModuleConfiguration("org.atmosphere", SonatypeSnapshotRepo),
    ModuleConfiguration("org.clapper", ScalaToolsReleases),
    ModuleConfiguration("org.codeahus.groovy", CodehausRepo),
    ModuleConfiguration("org.eclipse.jetty", sbt.DefaultMavenRepository),
    ModuleConfiguration("org.glassfish.grizzly", "Glassfish" at "http://maven.mojolly.com/content/repositories/glassfish-repo/"),
    ModuleConfiguration("org.guiceyfruit", GuiceyFruitRepo),
    ModuleConfiguration("org.jboss", JBossRepo),
    ModuleConfiguration("org.jboss.netty", JBossRepo),
    ModuleConfiguration("org.jboss.netty", JBossRepo),
    ModuleConfiguration("org.multiverse", CodehausRepo),
    ModuleConfiguration("org.scala-tools", "time", CasbahSnapshotRepo),
    ModuleConfiguration("com.codecommit", ScalaToolsReleases),
    ModuleConfiguration("org.scalatest", ScalaToolsReleases),
    ModuleConfiguration("org.scalaz", ScalaToolsReleases),
    ModuleConfiguration("cc.factorie", LocalM2),
    ModuleConfiguration("net.liftweb", ScalaToolsReleases),
    ModuleConfiguration("edu.umass.cs.iesl", LocalM2)
  )
}

object Dependencies {

  val backchatLibraryVersion = "0.3.3-SNAPSHOT"
  val slf4jVersion = "1.6.1"
  val specs2Version = "1.4"
  val elasticSearchVersion = "0.16.1"
  val gfServletApi = "3.1"
  val parboiledVersion = "1.0.0"
  val jedisVersion = "1.5.2"
  val asyncHttpClientVersion = "1.6.3"
  val dispatchVersion = "0.7.8"
  val tikaVersion = "0.8"
  val scalatraVersion = "2.0.0-SNAPSHOT"
  val grizzlyVersion = "2.1.1"
  def isBackchatSnapshot = backchatLibraryVersion.endsWith("-SNAPSHOT")

  val akkaVersion = "1.2-RC6"
  def akkaLib(s:String) = "se.scalablesolutions.akka" % ("akka-" + s)  % akkaVersion
  lazy val akkaLibs = Seq(akkaLib("actor"), akkaLib("remote"), akkaLib("stm"), akkaLib("actor"))

  val objenesis = "org.objenesis" % "objenesis" % "1.2"
  val libPhoneNumber = "com.google.libphonenumber" % "libphonenumber" % "2.4"

  val scalaTestVersion = "1.6.1"
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  val scalaCheck = "org.scala-tools.testing" %% "scalacheck" % "1.9" % "test"

  val specsVersion = "1.6.9"
  val specs = "org.scala-tools.testing" %% "specs" % specsVersion

  val specs2 = "org.specs2" %% "specs2" % specs2Version
  val slf4j = "org.slf4j" % "slf4j-api" % slf4jVersion
  val logbackClassic = "ch.qos.logback"     %   "logback-classic"     % "0.9.24"
  val logbackCore = "ch.qos.logback"     %   "logback-core"        % "0.9.24"

  // val commonsIo = "org.apache.commons" % "commons-io" % "2.0.1"
  val commonsIo = "commons-io" % "commons-io" % "2.0.1"

  val luceneVersion = "3.2.0"
  val luceneAnalyzers     = "org.apache.lucene" % "lucene-analyzers"    % luceneVersion
  val luceneBenchmark     = "org.apache.lucene" % "lucene-benchmark"    % luceneVersion
  val luceneCore          = "org.apache.lucene" % "lucene-core"         % luceneVersion
  val luceneDemo          = "org.apache.lucene" % "lucene-demo"         % luceneVersion
  val luceneGrouping      = "org.apache.lucene" % "lucene-grouping"     % luceneVersion
  val luceneHighlighter   = "org.apache.lucene" % "lucene-highlighter"  % luceneVersion
  val luceneIcu           = "org.apache.lucene" % "lucene-icu"          % luceneVersion
  val luceneInstantiated  = "org.apache.lucene" % "lucene-instantiated" % luceneVersion
  val luceneMemory        = "org.apache.lucene" % "lucene-memory"       % luceneVersion
  val luceneMisc          = "org.apache.lucene" % "lucene-misc"         % luceneVersion
  val luceneParent        = "org.apache.lucene" % "lucene-parent"       % luceneVersion
  val luceneQueries       = "org.apache.lucene" % "lucene-queries"      % luceneVersion
  val luceneQueryparser   = "org.apache.lucene" % "lucene-queryparser"  % luceneVersion
  val luceneRemote        = "org.apache.lucene" % "lucene-remote"       % luceneVersion
  val luceneSmartcn       = "org.apache.lucene" % "lucene-smartcn"      % luceneVersion
  val luceneSpatial       = "org.apache.lucene" % "lucene-spatial"      % luceneVersion
  val luceneSpellchecker  = "org.apache.lucene" % "lucene-spellchecker" % luceneVersion
  val luceneStempel       = "org.apache.lucene" % "lucene-stempel"      % luceneVersion
  val luceneWordnet       = "org.apache.lucene" % "lucene-wordnet"      % luceneVersion

  val liftVersion = "2.4-M4"
  val liftWebkit = "net.liftweb" %% "lift-webkit"  % liftVersion % "compile->default"
  val liftMapper = "net.liftweb" %% "lift-mapper"  % liftVersion % "compile->default"
  val liftWizard = "net.liftweb" %% "lift-wizard"  % liftVersion % "compile->default"
  val liftMongoDB = "net.liftweb" %% "lift-mongodb" % liftVersion % "compile->default"

  val scalazCore    = "org.scalaz"         %% "scalaz-core"          % "6.0.3"
  val scalaj        = "org.scalaj"         %% "scalaj-collection"    % "1.2"
  val factorie      = "cc.factorie"        % "factorie"             % "0.10.1-SNAPSHOT"

  val casbahVersion = "2.1.5.0"
  def casbahLib(s:String) = "com.mongodb.casbah" % ("casbah-" + s + "_2.9.0-1") % casbahVersion
  val casbahLibs = "core commons query".split(" ") map (l => casbahLib(l))

  val junit4        = "junit"              %  "junit"                % "4.4"

  val antiXML       = "com.codecommit"     %%  "anti-xml"          % "0.2"

  val neo4jVersion = "1.5.M02"
  val neo4j = "org.neo4j" % "neo4j" % neo4jVersion
  val neo4jAdvanced = "org.neo4j" % "neo4j-advanced" % neo4jVersion
  val neo4jShell = "org.neo4j" % "neo4j-shell" % neo4jVersion

  // val neo4jKernel = "org.neo4j" % "neo4j-kernel" % "1.0"
  // val neo4jUtils = "org.neo4j" % "neo4j-utils" % "1.0"

  val commonsIO = "commons-io" % "commons-io" % "1.3.2"

  val jettisonJson = "org.codehaus.jettison" % "jettison" % "1.3"

  val jdom = "org.jdom"                % "jdom"                % "1.1"
  val jaxen = "jaxen"                  % "jaxen"               % "1.1.1"
  val javaxServlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"

  // val jettyServletApi = "3.0.20100224"
  val jettyVersion = "8.0.0.M3"
  val jetty = "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container"
}


object Rexa2Build extends Build {

  val buildShellPrompt = ShellPrompt.buildShellPrompt

  import Resolvers._
  import Dependencies._
  import BuildSettings._

  val commonDeps:Seq[sbt.ModuleID] =
    casbahLibs ++ Seq(
      slf4j,
      logbackClassic,
      logbackCore,
      scalaTest,
      junit4,
      scalaCheck,
      factorie,
      scalazCore,
      scalaj,
      luceneAnalyzers,
      luceneCore,
      luceneQueries,
      "xerces" % "xercesImpl" % "2.9.1",
      luceneQueryparser,
      specs %"provided->default",
      antiXML,
      jettisonJson,
      commonsIO,
      neo4jAdvanced, neo4jShell,
      javaxServlet      
    )

  val webDeps = Seq(
    liftWebkit,
    liftMapper,
    liftWizard,
    liftMongoDB,
    jetty,
    javaxServlet 
  )

  val printClasspath = TaskKey[File]("print-class-path")

  def printCp = (target, fullClasspath in Compile, compile in Compile) map { (out, cp, analysis) =>
    println(cp.files.map(_.getName).mkString("\n"))
    println("----")
    println(analysis.relations.allBinaryDeps.toSeq.mkString("\n"))
    println("----")
    println(out)
    out
  }

  import com.typesafe.sbtaspectj.AspectjPlugin
  import com.typesafe.sbtaspectj.AspectjPlugin.{ Aspectj, inputs, aspectFilter }

  val aspectJSettings = buildSettings ++ AspectjPlugin.settings ++ Seq(
    resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
    inputs in Aspectj <<= update map { report =>
      report.matching(moduleFilter(organization = "se.scalablesolutions.akka", name = "akka-actor"))
                                    },
    aspectFilter in Aspectj := {
      (jar, aspects) => {
        if (jar.name.contains("akka-actor")) aspects filter (_.name.startsWith("Actor"))
        else Seq.empty[File]
      }
    },
    fullClasspath in Test <<= AspectjPlugin.useInstrumentedJars(Test),
    fullClasspath in Runtime <<= AspectjPlugin.useInstrumentedJars(Runtime)
  )

  lazy val rexa2:Project = Project(
    id = "rexa2",
    base = file("."),
    settings = buildSettings ++ aspectJSettings ++ Seq (libraryDependencies := commonDeps) 
    // settings = buildSettings ++ Seq (libraryDependencies := commonDeps)
  )

  val webSettings = seq(com.github.siasia.WebPlugin.webSettings: _*)
  def Conf = config("container")
	def jettyPort = 8080

  lazy val rexa2front:Project = Project (
    id = "front",
    base = file("front"),
    dependencies = Seq(rexa2),
    settings = buildSettings ++ Seq (libraryDependencies := commonDeps ++ webDeps) ++ webSettings ++ Seq(
		  port in Conf := jettyPort
    )
  )

}








// object SampleBuild extends Build {
//   lazy val sample = Project(
//     id = "sample",
//     base = file("."),
//     settings = Defaults.defaultSettings ++ AspectjPlugin.settings ++ Seq(
//       organization := "com.typesafe.sbtaspectj",
//       version := "0.1-SNAPSHOT",
//       scalaVersion := "2.9.1",
//       resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
//       libraryDependencies += "se.scalablesolutions.akka" % "akka-actor" % "1.2",
//       inputs in Aspectj <<= update map { report =>
//         report.matching(moduleFilter(organization = "se.scalablesolutions.akka", name = "akka-actor"))
//       },
//       aspectFilter in Aspectj := {
//         (jar, aspects) => {
//           if (jar.name.contains("akka-actor")) aspects filter (_.name.startsWith("Actor"))
//           else Seq.empty[File]
//         }
//       },
//       fullClasspath in Test <<= AspectjPlugin.useInstrumentedJars(Test),
//       fullClasspath in Runtime <<= AspectjPlugin.useInstrumentedJars(Runtime)
//     )
//   )
// }

