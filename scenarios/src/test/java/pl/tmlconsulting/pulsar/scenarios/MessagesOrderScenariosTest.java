package pl.tmlconsulting.pulsar.scenarios;


import static org.junit.jupiter.api.Assertions.assertEquals;

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
  void expectOrderedMessagesWhenAllAcknowledged() throws PulsarClientException {
    String topicName = TOPIC_NAME + System.currentTimeMillis();
    Producer<String> producer = client.newProducer(Schema.STRING)
        .topic(topicName)
        .create();

    producer.send("A");
    producer.send("B");
    producer.close();

    Consumer<String> testConsumer = client.newConsumer(Schema.STRING)
        .topic(topicName)
        .subscriptionName("unit-test")
        .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
        .subscriptionType(SubscriptionType.Exclusive)
        .subscribe();

    Message<String> messageAttempt1 = testConsumer.receive();
    testConsumer.acknowledge(messageAttempt1);
    Message<String> messageAttempt2 = testConsumer.receive();
    testConsumer.acknowledge(messageAttempt1);
    testConsumer.close();

    assertEquals("A", messageAttempt1.getValue());
    assertEquals("B", messageAttempt2.getValue());
  }

  private PulsarClient createClient() {
    try {
      return PulsarClient.builder()
          .serviceUrl("pulsar://localhost:6650")
          .build();
    } catch (PulsarClientException e) {
      throw new RuntimeException(e);
    }
  }
}
