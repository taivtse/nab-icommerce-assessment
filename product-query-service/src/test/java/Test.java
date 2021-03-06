import au.com.nab.icommerce.product.api.ProductQueryServiceGrpc;
import au.com.nab.icommerce.product.protobuf.PProductCriteriaRequest;
import au.com.nab.icommerce.product.protobuf.PProductsResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Test {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8201)
                .usePlaintext()
                .build();

        ProductQueryServiceGrpc.ProductQueryServiceBlockingStub blockingStub = ProductQueryServiceGrpc.newBlockingStub(channel);

        PProductCriteriaRequest criteria = PProductCriteriaRequest.newBuilder()
                .setName("Pro")
                .setPriceFrom(45000000)
                .setPriceTo(49000000)
                .setColor("gray")
                .setUnit("unit")
                .build();
        PProductsResponse response = blockingStub.getProductsByCriteria(criteria);
        System.out.println(response);

        channel.shutdown();
    }
}
