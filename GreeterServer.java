package com.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class GreeterServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50051)
                .addService(new GreeterImpl())
                .build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started on port 50051");
        server.awaitTermination();
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
            String replyMsg = "Hello, " + req.getName();
            HelloReply reply = HelloReply.newBuilder().setMessage(replyMsg).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}

public class GreeterClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

        HelloReply reply = stub.sayHello(HelloRequest.newBuilder().setName("Alice").build());
        System.out.println("Server Response: " + reply.getMessage());

        channel.shutdown();
    }
}

