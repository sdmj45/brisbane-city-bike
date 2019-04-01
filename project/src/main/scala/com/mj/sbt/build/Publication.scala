package com.mj.sbt.build

import sbt._

object Publication {

  def settings: Seq[Def.Setting[_]] = Seq(
    (Keys.publishTo in ThisBuild) := selectDestination((Keys.version in Keys.publish).value),
    (Keys.publishMavenStyle in ThisBuild) := true,
    (Keys.credentials in ThisBuild) += Credentials(Path.userHome / ".ivy2" / ".credentials")
  )

  //todo: change it
  val artifactoryUrl = "http://art.ebiznext.com/artifactory/"

  val releasesRepository: Resolver = "releases" at artifactoryUrl + "libs-release-local"

  val snapshotsRepository: Resolver = "snapshots" at artifactoryUrl + "libs-snapshot-local"

  private def selectDestination(version: String) =
    if (version.trim.toUpperCase.endsWith("SNAPSHOT")) Some(snapshotsRepository)
    else Some(releasesRepository)
}
