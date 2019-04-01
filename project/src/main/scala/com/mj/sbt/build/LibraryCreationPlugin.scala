package com.mj.sbt.build


import sbt.{AutoPlugin, Def, Plugins}
import sbtassembly.AssemblyPlugin

object LibraryCreationPlugin extends AutoPlugin {

  object autoImport {}

  override def projectSettings: Seq[Def.Setting[_]] = Dependencies.Library.settings ++ AssemblyDeployment.settings

  override def requires: Plugins = AssemblyPlugin

}
