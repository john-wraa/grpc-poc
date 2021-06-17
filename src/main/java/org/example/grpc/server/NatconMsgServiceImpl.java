package org.example.grpc.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.example.grpc.*;

@SuppressWarnings({"DuplicatedCode"})
@Slf4j
public class NatconMsgServiceImpl extends NatconMsgGrpc.NatconMsgImplBase {

    @Override
    public void aims(NatconMsgRequest request, StreamObserver<NatconMsgResponse> responseObserver) {
        var msgResponse = mock(request.getNatconMessage());
        if (msgResponse.getNatconMessage().getMsgNumber()>=0) {
            responseObserver.onNext(msgResponse);
        } else {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The message being sent is not recognized!")
                            .augmentDescription("The number that was sent was: " + request.getNatconMessage().getMsgNumber() + " We can only accept known messages.")
                            .asRuntimeException()
            );
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<NatconMsgRequest> aimsStream(StreamObserver<NatconMsgResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(NatconMsgRequest value) {
                var msgResponse = mock(value.getNatconMessage());
                if (msgResponse.getNatconMessage().getMsgNumber()>=0) {
                    responseObserver.onNext(msgResponse);
                } else {
                    responseObserver.onError(
                            Status.INVALID_ARGUMENT
                                    .withDescription("The message being sent is not recognized!")
                                    .augmentDescription("The number that was sent was: " + value.getNatconMessage().getMsgNumber() + " We can only accept known messages.")
                                    .asRuntimeException()
                    );
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void natcon(NatconMsgRequest request, StreamObserver<NatconMsgResponse> responseObserver) {
        var msgResponse = mock(request.getNatconMessage());
        if (msgResponse.getNatconMessage().getMsgNumber()>=0) {
            responseObserver.onNext(msgResponse);
        } else {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The message being sent is not recognized!")
                            .augmentDescription("The number that was sent was: " + request.getNatconMessage().getMsgNumber() + " We can only accept known messages.")
                            .asRuntimeException()
            );
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<NatconMsgRequest> natconStream(StreamObserver<NatconMsgResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(NatconMsgRequest value) {
                var msgResponse = mock(value.getNatconMessage());
                if (msgResponse.getNatconMessage().getMsgNumber()>=0) {
                    responseObserver.onNext(msgResponse);
                } else {
                    responseObserver.onError(
                            Status.INVALID_ARGUMENT
                                    .withDescription("The message being sent is not recognized!")
                                    .augmentDescription("The number that was sent was: " + value.getNatconMessage().getMsgNumber() + " We can only accept known messages.")
                                    .asRuntimeException()
                    );
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    private NatconMsgResponse mock(NatconMessage natconMessage) {
        System.out.println("Received " + natconMessage.getMsgNumber());
        switch (natconMessage.getMsgNumber()) {
            case 9393:
                return NatconMsgResponse.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8539).build()).build();
            case 8539:
                return NatconMsgResponse.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8510).build()).build();
            case 8540:
                return NatconMsgResponse.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8511).build()).build();
            case 8510:
                return NatconMsgResponse.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8540).build()).build();
            case 8511:
                return NatconMsgResponse.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8705).build()).build();
            case 8705:
                return NatconMsgResponse.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(8706).build()).build();
            case 8706:
                return NatconMsgResponse.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(0).build()).build();
            default:
                return NatconMsgResponse.newBuilder().setNatconMessage(NatconMessage.newBuilder().setMsgNumber(-1).build()).build();
        }




    }


}
