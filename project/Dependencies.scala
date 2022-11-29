import sbt._
import sbt.Keys._

object Dependencies extends AutoPlugin {
  override def projectSettings: Seq[Def.Setting[_]] =
    super.projectSettings ++ Seq(
      libraryDependencies ++=
        Seq(
          cats,
          shapeless,
          reflection
      ).flatten
    )

  val cats =
    Seq(
      "cats-core",
    ).map("org.typelevel" %% _ % "2.9.0")

  val shapeless =
    Seq(
      "shapeless"
    ).map("com.chuusai" %% _ % "2.3.3")

  val reflection =
    Seq("scala-reflect")
    .map("org.scala-lang" % _ % "2.13.8")
}
