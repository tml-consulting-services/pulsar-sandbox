package org.apache.pulsar.functions.watermark;

import org.apache.pulsar.functions.windowing.TimestampExtractor;

public class SampleTimestampExtractor implements TimestampExtractor<String> {

  @Override
  public long extractTimestamp(String input) {
    return 0;
  }
}
