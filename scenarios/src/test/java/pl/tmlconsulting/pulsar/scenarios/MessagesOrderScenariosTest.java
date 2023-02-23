package pl.tmlconsulting.pulsar.scenarios;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.client.api.SubscriptionType;
import org.junit.jupiter.api.Test;

public class MessagesOrderScenariosTest {

  public static final String TOPIC_NAME = "order_test";

  private final PulsarClient client = createClient();

  @Test
  void expectMessagesInOrderWhenAck() throws PulsarClientException {
    String topicName = TOPIC_NAME + System.currentTimeMillis();
    Producer<String> producer = getProducer(topicName);

    producer.send("A");
    producer.send("B");
    producer.close();

    Consumer<String> consumer = client.newConsumer(Schema.STRING).topic(topicName)
        .subscriptionName("unit-test")
        .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
        .subscriptionType(SubscriptionType.Exclusive).subscribe();

    Message<String> m1 = consumer.receive();
    consumer.acknowledge(m1);
    Message<String> m2 = consumer.receive();
    consumer.acknowledge(m1);
    consumer.close();

    assertEquals("A", m1.getValue());
    assertEquals("B", m2.getValue());
  }

  @Test
  void expectMessageRedeliveryWithDelayWhenNegativeAck() throws PulsarClientException {
    String topicName = TOPIC_NAME + System.currentTimeMillis();
    Producer<String> producer = getProducer(topicName);

    producer.send("A");
    producer.send("B");

    Consumer<String> consumer = client.newConsumer(Schema.STRING).topic(topicName)
        .subscriptionName("unit-test")
        .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
        .subscriptionType(SubscriptionType.Exclusive)
        .negativeAckRedeliveryDelay(1, TimeUnit.SECONDS).subscribe();

    Message<String> m1 = consumer.receive();
    consumer.negativeAcknowledge(m1);
    Message<String> m2 = consumer.receive();
    consumer.acknowledge(m2);
    producer.send("C");
    Message<String> m3 = consumer.receive();
    consumer.acknowledge(m3);
    Message<String> m4 = consumer.receive();
    consumer.acknowledge(m4);

    assertEquals("A", m1.getValue());
    assertEquals("B", m2.getValue());
    assertEquals("C", m3.getValue());
    assertEquals("A", m4.getValue());

    producer.close();
    consumer.close();
  }

  private Producer<String> getProducer(String topicName) throws PulsarClientException {
    return client.newProducer(Schema.STRING).topic(topicName).create();
  }

  private PulsarClient createClient() {
    try {
      return PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
    } catch (PulsarClientException e) {
      throw new RuntimeException(e);
    }
  }
}
