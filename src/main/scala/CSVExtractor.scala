import scala.io.Source
import java.io.File

object CSVExtractor {

  def process(directoryPath: String) = {
    val files = getFiles(directoryPath)
    val result =
      if (!files.isEmpty)
        files.foldLeft(Map[String, StatsAggregator]())(
          (acc, file) => processFile(file, acc)
        )
      else Map()
    printResults(files.length, result)
  }

  def processFile(
    file: File,
    resultMap: Map[String, StatsAggregator]
  ): Map[String, StatsAggregator] = {
    val bufferedSource = Source.fromFile(file)
    try {
      bufferedSource.getLines
        .drop(1)
        .foldLeft(resultMap)((acc, line) => {
          processLine(acc, line)
        })
    } finally {
      bufferedSource.close()
    }
  }

  private def processLine(resultMap: Map[String, StatsAggregator],
                          line: String)  = {
    val fields = line.split(",").map(_.trim)
    fields(1) match {
      case "NaN" =>
        resultMap.get(fields(0)) match {
          case Some(value) => resultMap + (fields(0) -> value.failed())
          case _           => resultMap + (fields(0) -> StatsAggregator().failed())
        }
      case stats =>
        resultMap.get(fields(0)) match {
          case Some(value) =>
            resultMap + (fields(0) -> (value + stats.toInt))
          case _ =>
            resultMap + ((fields(0) -> StatsAggregator(stats.toInt)))
        }
    }
  }

  private def getFiles(directoryPath: String): Array[File] = {
    val directory = new File(directoryPath)
    if (!directory.exists || !directory.isDirectory) {
      throw new IllegalArgumentException(
        "The provided path is not a valid directory."
      )
    }
    directory.listFiles.filter(_.getName.endsWith(".csv"))
  }

  private def printResults(numberOfFiles: Int,
                           result: Map[_ <: String, StatsAggregator]): Unit = {
    println("Num of processed files: " + numberOfFiles)
    println(
      "Num of processed measurements: " + result.foldLeft(0)(
        (acc, entry) => acc + entry._2.successCount + entry._2.failedCount
      )
    )
    println(
      "Num of failed measurements: " + result
        .foldLeft(0)((acc, entry) => acc + entry._2.failedCount)
    )
    println("Sensors with highest avg humidity: ")
    println("sensor-id,min,avg,max")
    for ((k, v) <- result.toSeq.sortWith(_._2.avg > _._2.avg)) {
      if (v.successCount > 0)
        println(k + "," + v.min + "," + v.avg + "," + v.max)
      else
        println(k + ",NaN,NaN,NaN")
    }
  }

}
