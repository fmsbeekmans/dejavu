lazy val root = (project in file(""))
  .settings(
    name := "dejavu"
  )
  .enablePlugins(Dependencies)
  .enablePlugins(ScalaSettings)
  .enablePlugins(ScalafmtSettings)
  .enablePlugins(TestSettings)
