import Common._
import com.mj.sbt.build._

/////////////////////////////////
// Defaults
/////////////////////////////////

com.mj.sbt.build.Defaults.settings

com.mj.sbt.build.Publication.settings

/////////////////////////////////
// Useful aliases
/////////////////////////////////

addCommandAlias("cd", "project") // navigate the projects

addCommandAlias("cc", ";clean;compile") // clean and compile

addCommandAlias("pl", ";clean;publishLocal") // clean and publish locally

addCommandAlias("pr", ";clean;publish") // clean and publish globally

addCommandAlias("pld", ";clean;local:publishLocal;dockerComposeUp") // clean and publish/launch the docker environment


/////////////////////////////////
// Resolvers
/////////////////////////////////

resolvers in ThisBuild ++= Seq(
  "conjars.org" at "http://conjars.org/repo",
  "cakesolutions" at "http://dl.bintray.com/cakesolutions/maven/",
  "bintray-backline-open-source-releases" at "https://dl.bintray.com/backline/open-source",
  "lightshed-maven" at "http://dl.bintray.com/content/lightshed/maven",
  "confluent" at "http://packages.confluent.io/maven/",
  Resolver.file("local project resolver", file("jars"))(Resolver.ivyStylePatterns)
)

/////////////////////////////////
// Sub projects definitions
/////////////////////////////////

concurrentRestrictions in Global := {
  Tags.limit(Tags.Test, 1) ::
    Tags.limit(Tags.Publish, 1) :: Nil
}

val common = file("src") / "common"
lazy val commonLibrary = library("common", common / "common")

val scoringNode = file("src") / "scoring-node"
lazy val scoringNodeService = service("scoring-node", scoringNode / "scoring-node").dependsOn(commonLibrary)

val trainingNode = file("src") / "training-node"
lazy val trainingNodeService = service("training-node", trainingNode / "training-node").dependsOn(commonLibrary)