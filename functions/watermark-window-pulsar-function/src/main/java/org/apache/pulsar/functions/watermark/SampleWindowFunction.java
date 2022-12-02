package org.apache.pulsar.functions.watermark;

import java.util.Collection;
import org.apache.pulsar.functions.api.Record;
import org.apache.pulsar.functions.api.WindowContext;
import org.apache.pulsar.functions.api.WindowFunction;

public class SampleWindowFunction implements WindowFunction<String, Void> {

  @Override
  public Void process(Collection<Record<String>> input, WindowContext context) throws Exception {
    context.getLogger().info("Executed now! *********");
    return null;
  }
}
