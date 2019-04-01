package com.mj.sbt.build

import sbt._
import sbtbuildinfo.BuildInfoPlugin

trait Definition {

  /**
    * Creates a library module
    *
    * @param name the name of the module, will be used as the module's root folder name
    * @return the module's `Project`
    */
  def library(name: String): Project = library(name, file(name))

  /**
    * Creates a library sub project
    *
    * @param name the name of the project
    * @param src  the module's root folder
    * @return the module's `Project`
    */
  def library(name: String, src: File): Project =
    sbt
      .Project(id = name, base = src)
      .enablePlugins(LibraryCreationPlugin)
      .configs(IntegrationTest)
      .settings(sbt.Defaults.itSettings: _*)

  /**
    * Creates a micro-service module with predefined dependencies
    *
    * @see com.mj.sbt.build.Dependencies.Service for the list of predefined dependencies
    * @param name the name of the module, will be used as the module's root folder name
    * @return the module's `Project`
    */
  def service(name: String): Project = service(name, file(name))

  /**
    * Creates a micro-service module with predefined dependencies
    *
    * @see com.mj.sbt.build.Dependencies.Service for the list of predefined dependencies
    * @param name the name of the module
    * @param src  the module's root folder
    * @return the module's `Project`
    */
  def service(name: String, src: File): Project =
    sbt
      .Project(id = name, base = src)
      .enablePlugins(ServiceCreationPlugin, BuildInfoPlugin)
      .configs(IntegrationTest)
      .settings(sbt.Defaults.itSettings: _*)

}
