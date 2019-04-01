package com.mj.sbt.build

import com.mj.sbt.build.ProjectInitializer.autoImport._
import sbt.Keys._
import sbt.{Keys, _}

object AnsibleDeployment {

  def settings = Seq(
    templateMappings in init := Seq(
      file("templates") / "ansible-common" -> baseDirectory.value / "ansible"
    ),
    templateParameters := {
      val projectPath = (Keys.baseDirectory in ThisProject).value.toPath
      val ansibleRootPath = ((Keys.baseDirectory in ThisBuild).value / "ansible").toPath
      val ansiblePath = ((Keys.baseDirectory in ThisProject).value / "ansible").toPath
      Map(
        "serviceName"                -> name.value,
        "package"                    -> (organization.value + "." + moduleName.value.filter(_.isLetterOrDigit)),
        "ansibleRelativePath"        -> ansiblePath.relativize(ansibleRootPath).toString,
        "ansibleReverseRelativePath" -> ansibleRootPath.relativize(ansiblePath).toString,
        "projectRelativePath"        -> projectPath.relativize((Keys.baseDirectory in ThisBuild).value.toPath).toString
      )
    }
  )
}
