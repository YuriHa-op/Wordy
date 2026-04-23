package server;

import hello.GreetingServiceGrpc;
import hello.HelloResponse;
import hello.HelloRequest;
import io.grpc.stub.StreamObserver;
import java.util.logging.Logger;

public class GrpcServant extends GreetingServiceGrpc.GreetingServiceImplBase {
    private static final Logger logger = Logger.getLogger(GrpcServant.class.getName());

   @Override
   public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
       String name = request.getName();
       logger.info("Received sayHello request for name: " + name);
       
       HelloResponse response = HelloResponse.newBuilder()
               .setGreeting("Hello, " + name + ". Welcome to gRPC!")
               .build();

       logger.info("Sending response: " + response.getGreeting());
       responseObserver.onNext(response); // Send response
       responseObserver.onCompleted();   // Close call
   }
}
