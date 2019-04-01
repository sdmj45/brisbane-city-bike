package com.mj.sbt.build

import com.tapad.docker.DockerComposePlugin.autoImport._
import com.typesafe.sbt.SbtGit.GitKeys
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.SbtNativePackager.Docker
import com.typesafe.sbt.packager.docker.{Cmd, DockerPlugin, ExecCmd}
import sbt._
import sbt.Keys._


/**
  * This is just an example, we do not use it in this project
  */
trait Packager {
  lazy val packager = TaskKey[Unit]("packager", "Unified packaging")
}

object DockerPackaging extends Packager {

  val Local = config("local")
  val dockerFile = taskKey[File]("Produces the DockerFile")
  val POBuild = config("po-build")

  val DockerTag = Tags.Tag("docker")

  def settings = Seq(
    dockerRepository := sys.env.get("DOCKER_REGISTRY_VIP").orElse(Some(Defaults.defaultDockerRepository)),
    // short-circuit the DockerPlugin to override the local:publishLocal task so that
    // it publishes the artifact as a local docker image with tag "local"
    (publishLocal in Local) := {
      DockerPlugin.publishLocalDocker((stage in Docker).value, packageName.value + ":local" :: Nil, streams.value.log)
    },
    // short-circuit the DockerPlugin to override the po-build:publishLocal task so that
    // it publishes the artifact as a local docker image with tag set to the current branch name
    (packager in POBuild) := {
      val context = (stage in Docker).value
      val target = packageName.value
      val gitReader = GitKeys.gitReader.value
      val repo = dockerRepository.value
      val branch = gitReader.withGit(_.branch).replaceAllLiterally("/", "_")
      val tag = repo.map(_ + "/").getOrElse("") + target + ":" + branch
      val log = streams.value.log
      val cmd = Seq("docker", "build", "--force-rm", "-t", tag, ".")
      DockerPlugin.publishLocalDocker(context, cmd, log)
    },
    // when using docker:publishLocal, the produced image's tag will be set to the sha1 of the last git commit
    (version in Docker) := GitKeys.gitHeadCommit.value.getOrElse(version.value),
    dockerImageCreationTask := (Keys.publishLocal in Docker),
    (version in publish) := (version in ThisBuild).value,
    dockerExposedVolumes := Seq("config", "logs", "/native/lib", "/security"),
    dockerFile := (target in (stage in Docker)).value / "DockerFile",
    scriptClasspath += "FIXME:/config/:/hadoop-config/",
    dockerCommands := Seq(
      Cmd("FROM", "openjdk:8-jre"),
      Cmd(
        "RUN",
        "addgroup smartlake --gid 4200 && useradd -u 4201 -G smartlake -m -s /bin/bash dev && useradd -u 4202 -G smartlake -m -s /bin/bash itg && useradd -u 4203 -G smartlake -m -s /bin/bash prp && useradd -u 4204 -G smartlake -m -s /bin/bash run && mkdir -p /config /native/lib /security && chown -R :smartlake /config /native/lib /security"
      ),
      Cmd("VOLUME", "/native/lib /hadoop-config"),
      Cmd("ENV", s"DOCKER_REGISTRY_VIP ${Defaults.defaultDockerRepository}"),
      Cmd("ENV", s"SCHEMA_REGISTRY_URL ${Defaults.defaultSchemaRegistryUrl}"),
      Cmd("ENV", "LD_LIBRARY_PATH /native/lib"),
      Cmd("ENV", "MESOS_NATIVE_JAVA_LIBRARY /native/lib/libmesos.so"),
      Cmd("ENV", "SMARTLAKE_ENV dev"),
      Cmd("ENV", "SMARTLAKE_KEYTAB_URL http://leader.mesos/secrets"),
      Cmd("ENV", "HADOOP_CONF_DIR /hadoop-config"),
      Cmd("ENV", "SERVICE_CONFIG_DIR /config"),
      Cmd("ENV", "SECURITY_SERVICE_DIR /security"),
      Cmd("ENV", "MESOS_SANDBOX /mnt/mesos/sandbox"),
      Cmd("WORKDIR", "/mnt/mesos/sandbox"),
      Cmd(
        "ENTRYPOINT",
        "mkdir -p /config && cp -R $MESOS_SANDBOX$SERVICE_CONFIG_DIR/* /config && mkdir -p /security && cp -R $MESOS_SANDBOX$SECURITY_SERVICE_DIR/* /security && cp $MESOS_SANDBOX/*.keytab /security && chmod 444 /security/*.keytab && chown $SMARTLAKE_ENV /security/*.keytab && su -m $SMARTLAKE_ENV -c " + dockerEntrypoint.value.head
      ),
      Cmd("ADD", "opt /opt"),
      Cmd("RUN", "chown -R :smartlake /opt && chmod +x -R /opt")
    ),
    packager := (Keys.publishLocal in Docker).value
  )
}
