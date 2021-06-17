package org.example.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.example.grpc.NatconMessage;
import org.example.grpc.NatconMsgGrpc;
import org.example.grpc.NatconMsgRequest;
import org.example.grpc.NatconMsgResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NatconMsgClient {
    private List<NatconMsgRequest> respond = new ArrayList<>();
    public static void main(String[] args) {
        System.out.println("Natcon gRPC Client started");
        NatconMsgClient main = new NatconMsgClient();
        main.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();
        int runs = 0;
        while (runs<10) {
            doBiDiStreamingCall(channel);
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runs++;
        }
        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    private void doBiDiStreamingCall(ManagedChannel channel) {
        NatconMsgGrpc.NatconMsgStub asyncClient =NatconMsgGrpc.newStub(channel);
        var latch = new CountDownLatch(1);
        StreamObserver<NatconMsgRequest> requestObserver = asyncClient.natconStream(new StreamObserver<>() {
            @Override
            public void onNext(NatconMsgResponse value) {
                var currenMessageNumber = value.getNatconMessage().getMsgNumber();
                System.out.println("Received " + currenMessageNumber + " from Natcon");
                System.out.println("Produce to Kafka Topic: " + currenMessageNumber);
                switch (currenMessageNumber) {
                    case 8539:
                        respond.add(NatconMsgRequest.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8510).build()).build());
                        break;
                    case 8540:
                        respond.add(NatconMsgRequest.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8511).build()).build());
                        break;
                    case 8705:
                        respond.add(NatconMsgRequest.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8706).build()).build());
                        break;
                    default:
                        respond.add(NatconMsgRequest.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(9393).build()).build());
                        break;
                }

            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending data");
                latch.countDown();
            }
        });
        while (!respond.isEmpty()) {
            respond.forEach(natconMsgRequest -> {
                System.out.println("Consume from Kafka Topic: " + natconMsgRequest.getNatconMessage().getMsgNumber());
                System.out.println("Send " + natconMsgRequest.getNatconMessage().getMsgNumber() + " to Natcon");
                requestObserver.onNext(natconMsgRequest);
            });
            respond.clear();
        }
        System.out.println("Sending 9393 message");
        requestObserver.onNext(NatconMsgRequest.newBuilder()
                .setNatconMessage(
                        NatconMessage.newBuilder().setMsgNumber(9393).build()
                )
                .build());

        try {
            latch.await(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}