package org.examples.spark.utils

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

object HdfsUtils {

  def listFolderNamesInFolder(hdfsPath: String): List[String] =
    FileSystem
      .get(new Configuration())
      .listStatus(new Path(hdfsPath))
      .flatMap(status => if (!status.isFile) Some(status.getPath.getName) else None)
      .toList


  def moveFile(oldPath: String, newPath: String): Unit = {
    val fileSystem = FileSystem.get(new Configuration())
    fileSystem.rename(new Path(oldPath), new Path(newPath))
  }

  def createFolder(hdfsPath: String): Unit =
    FileSystem.get(new Configuration()).mkdirs(new Path(hdfsPath))

  def moveTweetFiles(hdfsPath: String): Unit =
    listFolderNamesInFolder(s"$hdfsPath/temp/Tweets").foreach {
      case userid =>
        createFolder(s"$hdfsPath/final/$userid")
        moveFile(
          s"$hdfsPath/temp/Tweets/$userid/Tweets.csv",
          s"$hdfsPath/final/$userid/Tweets.csv")
    }

  def moveMentionsFiles(hdfsPath: String): Unit =
    listFolderNamesInFolder(s"$hdfsPath/temp/Mentions").foreach {
      case userid =>
        createFolder(s"$hdfsPath/final/$userid")
        moveFile(
          s"$hdfsPath/temp/Mentions/$userid/Mentions.csv",
          s"$hdfsPath/final/$userid/Mentions.csv")
    }
}
