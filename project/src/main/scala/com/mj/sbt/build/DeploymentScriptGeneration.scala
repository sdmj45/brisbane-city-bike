package com.mj.sbt.build

import com.typesafe.sbt.SbtGit.GitKeys
import com.typesafe.sbt.git.{GitRunner, NullLogger}
import sbt.Keys.{baseDirectory, _}
import sbt._

trait DeploymentScriptGeneration {
  val generateDeployScript = taskKey[Unit]("Produces the groovy deploy script")
  val services = SettingKey[Seq[File]]("services")
  val jobs = SettingKey[Seq[String]]("jobs")

  def deployScriptSettings = Seq(
    (services in ThisBuild) := ((services in ThisBuild) ?? Seq()).value,
    (generateDeployScript in ThisBuild) := DeploymentScriptGeneration.updateDeployScript(
      (baseDirectory in ThisBuild).value,
      services.value,
      streams.value.log,
      GitKeys.gitRunner.value
    )
  )
}

object DeploymentScriptGeneration {

  def updateDeployScript(baseDirectory: File, services: Seq[File], log: Logger, gitRunner: GitRunner) = {
    val serviceList = services
      .map(_ / "ansible")
      .map(dir => baseDirectory.toPath.relativize(dir.toPath))
      .map(_.toString)
      .sorted
      .map("'" + _ + "'")
      .mkString(",\n")
    //val deployScript = G8.apply(baseDirectory / "templates" / "nightly" / "Jenkinsfile", file("Jenkinsfile"), baseDirectory, Map("services" -> serviceList)).toList
    //gitRunner.apply("add" :: deployScript.map(_.toPath.toString):_*)(baseDirectory, log)
  }

}
