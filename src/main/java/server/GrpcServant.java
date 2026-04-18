package server;

import hello.GreetingServiceGrpc;
import hello.HelloResponse;
import hello.HelloRequest;
import io.grpc.stub.StreamObserver;

public class GrpcServant extends GreetingServiceGrpc.GreetingServiceImplBase {
   @Override
   public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
       String name = request.getName();
       HelloResponse response = HelloResponse.newBuilder()
               .setGreeting("Hello, " + name + ". Welcome to gRPC!")
               .build();

       responseObserver.onNext(response); // Send response
       responseObserver.onCompleted();   // Close call
   }
}

