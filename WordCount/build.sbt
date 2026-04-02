name := "WordCount"
version := "1.0"
scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % "3.3.6" % "provided"
)

assembly / mainClass := Some("WordCount")
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
