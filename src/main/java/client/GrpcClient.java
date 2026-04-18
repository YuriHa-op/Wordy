package client;

import hello.GreetingServiceGrpc;
import hello.HelloRequest;
import hello.HelloResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
   public static void main(String[] args) {
       ManagedChannel channel = ManagedChannelBuilder
               .forAddress("localhost", 8080)
               .usePlaintext()
               .build();

       GreetingServiceGrpc.GreetingServiceBlockingStub stub =
               GreetingServiceGrpc.newBlockingStub(channel);
       HelloRequest request = HelloRequest.newBuilder()
               .setName("Juan dela Cruz").build();

       HelloResponse response = stub.sayHello(request);
       System.out.println("Server response: " + response.getGreeting());
       channel.shutdown();
   }
}

