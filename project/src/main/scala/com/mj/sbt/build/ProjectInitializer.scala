package com.mj.sbt.build

import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, FileVisitor, Files, Path}

import com.typesafe.sbt.git.GitRunner
import com.typesafe.sbt.{GitPlugin, GitVersioning, SbtGit}
import sbt._

import scala.collection.mutable

case class ProjectInitData(
  name: String,
  baseDirectory: File,
  templateRoot: File,
  scalaSource: File,
  organization: String,
  version: String
)

case class GitSession(runner: GitRunner, cwd: File, logger: Logger) {

  def apply(command: String, args: String*): String = runner(command :: args.toList: _*)(cwd, logger)

  def add(path: String) = apply("add", path)

  def status = apply("status")
}

object ProjectInitializer extends AutoPlugin {

  object autoImport {

    lazy val templateMappings = SettingKey[Seq[(File, File)]]("template-mappings", "The root of the project template")

    lazy val templateParameters =
      SettingKey[Map[String, String]]("template-parameters", "The parameters needed in the templates")

    lazy val init = TaskKey[Unit]("init", "Initializes the akka-http service with the basic skeleton")

  }

  import autoImport._

  override def projectSettings =
    Seq(
      init :=
        ProjectCreator.bootstrap(
          (templateMappings in init).value,
          GitSession(
            SbtGit.GitKeys.gitRunner.value,
            (Keys.baseDirectory in ThisProject).value,
            Keys.streams.value.log
          ),
          templateParameters.value
        )
    )

  override def requires: Plugins = GitVersioning && GitPlugin
}


object ProjectCreator {

  def bootstrap(mappings: Seq[(File, File)], gitSession: GitSession, params: Map[String, String]): Unit = {
    mappings.foreach{case (src, dst) =>
      val visitor = new MappingVisitor()
      Files.walkFileTree(src.toPath, visitor)
      //val files = G8.apply(visitor.result, dst , params)
      //files.foreach(f => gitSession.add(f.toString))
    }
    gitSession.status
  }



}

class MappingVisitor() extends FileVisitor[Path] {

  val sep = System.getProperty("file.separator")
  val accum = mutable.ListBuffer.empty[(File, String)]
  val destination = mutable.ListBuffer.empty[String]

  override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = FileVisitResult.TERMINATE

  override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
    accum append (file.toFile -> (destination.drop(1).mkString(sep) + sep + file.getFileName.toString))
    FileVisitResult.CONTINUE
  }

  override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
    destination.append(dir.getFileName.toString)
    FileVisitResult.CONTINUE
  }

  override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
    destination.remove(destination.length - 1)
    FileVisitResult.CONTINUE
  }

  def result = accum.toList
}
