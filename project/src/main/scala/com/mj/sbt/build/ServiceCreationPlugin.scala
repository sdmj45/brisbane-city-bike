package com.mj.sbt.build

import com.mj.sbt.build.ProjectInitializer.autoImport._
import com.tapad.docker.DockerComposePlugin
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoPlugin

object ServiceCreationPlugin
    extends AutoPlugin
    with DeploymentScriptGeneration {

  object autoImport extends DependencyBundles

  override def projectSettings: Seq[Def.Setting[_]] = {
    Dependencies.Service.settings ++
      AssemblyDeployment.settings ++
      BuildInfoSettings.settings ++
      AnsibleDeployment.settings ++
      Seq(
        templateMappings in init += file("templates") / "service" -> baseDirectory.value,
        dockerExposedPorts := Seq(9000, 5050)
      )
  }

  override def requires: Plugins =
    DockerComposePlugin && JavaAppPackaging && ProjectInitializer && BuildInfoPlugin
}
