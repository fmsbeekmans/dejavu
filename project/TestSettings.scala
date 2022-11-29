import sbt._
import sbt.Keys._

object TestSettings extends AutoPlugin {


  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
//      libraryDependencies ++= Seq(
//      ).flatten.map(_ % Test),
      parallelExecution in Test in ThisBuild := false,
      dependencyOverrides += "com.github.jnr" % "jnr-posix" % "3.1.8",
      fork in Test in ThisBuild := true,
      javaOptions in Test in ThisBuild ++= Seq(
        "-Djava.util.logging.config.file=logging.properties"
      )
    )

}
