package com.alertysense.alertysensepubsubclient;

import com.alertysense.pubsub.client.Message;
import com.alertysense.pubsub.client.PublisherServiceGrpc;
import com.alertysense.pubsub.client.Response;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public abstract class AlertySensePubSubClient {

    private final String host;
    private final Integer port;
    ManagedChannel managedChannel;
    private PublisherServiceGrpc.PublisherServiceFutureStub publisherServiceFutureStub;


    protected AlertySensePubSubClient(String host, Integer port) throws Exception {
        this.host = host;
        this.port = port;
        this.managedChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        if (managedChannel.getState(true).equals(ConnectivityState.READY)) {
            log.info("Connected to the server");
        } else {
            log.error("Failed to connect to the server");
            throw new Exception("Failed to connect to the server");
        }

    }

    public Response publish(String message, String topic) throws Exception {
        try {
            publisherServiceFutureStub = PublisherServiceGrpc.newFutureStub(managedChannel);
            publisherServiceFutureStub.publishMessage(Message.newBuilder().setMessage(message).setTopic(topic).build());

            return null;
        } catch (Exception e) {
            return null;
        }

    }
}
