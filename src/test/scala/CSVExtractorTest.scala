import org.scalatest.funsuite.AnyFunSuite

import java.io.File

class CSVExtractorTest extends AnyFunSuite {

  test("processFile should return empty map for empty CSV file") {
    val result = CSVExtractor.processFile(
      new File("src/test/resources/empty_csv/empty.csv"),
      Map()
    )
    assert(result == Map())
  }

  test(
    "process should throw IllegalArgumentException for non-existent directory"
  ) {
    assertThrows[IllegalArgumentException] {
      CSVExtractor.process("src/test/resources/non_existent")
    }
  }

  test("processFile should return correct stats for CSV file with data") {
    val result = CSVExtractor.processFile(
      new File("src/test/resources/test_csvs/test1.csv"),
      Map()
    )
    assert(result.nonEmpty)
    val expected = Map(
      "s1" -> StatsAggregator(20, 10, 30, 3, 0),
      "s2" -> StatsAggregator(89, 88, 90, 2, 0)
    )
    assert(result == expected)
  }

  test("processFile should handle CSV with 0 values correctly") {
    val result = CSVExtractor.processFile(
      new File("src/test/resources/test_csvs/test2.csv"),
      Map()
    )
    assert(result.nonEmpty)
    val expected = Map(
      "s2" -> StatsAggregator(79, 78, 80, 2, 0),
      "s50" -> StatsAggregator(0, 0, 0, 2, 0),
      "s1" -> StatsAggregator(98, 98, 98, 1, 0)
    )
    assert(result == expected)
  }

  test("processFile should handle CSV with NaN values correctly") {
    val result = CSVExtractor.processFile(
      new File("src/test/resources/test_csvs/test3.csv"),
      Map()
    )
    assert(result.nonEmpty)
    val expected = Map(
      "s2" -> StatsAggregator(79, 78, 80, 2, 0),
      "s3" -> StatsAggregator(-1, 0, 0, 0, 2),
      "s1" -> StatsAggregator(98, 98, 98, 1, 0)
    )
    assert(result == expected)
  }

}
