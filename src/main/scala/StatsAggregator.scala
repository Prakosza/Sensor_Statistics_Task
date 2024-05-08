case class StatsAggregator(avg: Int = -1,
                           min: Int = 0,
                           max: Int = 0,
                           successCount: Int = 0,
                           failedCount: Int = 0) {
  def +(value: Int): StatsAggregator = {
    StatsAggregator(
      avg = if(avg == -1) value else (avg * successCount + value) / (successCount + 1),
      min = if (value < min) value else min,
      max = if (value > max) value else max,
      successCount = successCount + 1,
      failedCount = failedCount
    )
  }
  def failed(): StatsAggregator = {
    StatsAggregator(
      avg = avg,
      min = min,
      max = max,
      successCount = successCount,
      failedCount = failedCount + 1
    )
  }
}
object StatsAggregator {
  def apply(value: Int): StatsAggregator = {
    StatsAggregator(value, value, value, 1)
  }
}
