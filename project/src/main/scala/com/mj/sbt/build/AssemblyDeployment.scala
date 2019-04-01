package com.mj.sbt.build

import sbt.Keys.test
import sbtassembly.AssemblyKeys.{
  assembly,
  assemblyMergeStrategy,
  assemblyOption
}
import sbtassembly.{MergeStrategy, PathList}

object AssemblyDeployment {

  def settings = Seq(
    assemblyOption in assembly := (assemblyOption in assembly).value
      .copy(includeScala = false),
    test in assembly := {},
    assemblyMergeStrategy in assembly := {
      case PathList("org", "aopalliance", xs @ _*)      => MergeStrategy.last
      case PathList("ch", "qos", xs @ _*)               => MergeStrategy.last
      case PathList("javax", "inject", xs @ _*)         => MergeStrategy.last
      case PathList("javax", "servlet", xs @ _*)        => MergeStrategy.last
      case PathList("javax", "el", xs @ _*)             => MergeStrategy.last
      case PathList("javax", "activation", xs @ _*)     => MergeStrategy.last
      case PathList("org", "apache", xs @ _*)           => MergeStrategy.last
      case PathList("com", "google", xs @ _*)           => MergeStrategy.last
      case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
      case PathList("com", "codahale", xs @ _*)         => MergeStrategy.last
      case PathList("com", "yammer", xs @ _*)           => MergeStrategy.last
      case "about.html"                                 => MergeStrategy.rename
      case "META-INF/ECLIPSEF.RSA"                      => MergeStrategy.last
      case "META-INF/mailcap"                           => MergeStrategy.last
      case "META-INF/mimetypes.default"                 => MergeStrategy.last
      case "META-INF/io.netty.versions.properties"      => MergeStrategy.last
      case "plugin.properties"                          => MergeStrategy.last
      case "log4j.properties"                           => MergeStrategy.last
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
}
