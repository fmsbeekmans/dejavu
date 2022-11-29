import sbt._
import org.scalafmt.sbt.ScalafmtPlugin
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._

object ScalafmtSettings extends AutoPlugin {

  override def requires = ScalafmtPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    scalafmtOnCompile in ThisBuild := true
  )
}

