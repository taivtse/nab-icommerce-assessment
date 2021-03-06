package au.com.nab.icommerce.api.gateway.mapper.response;

import au.com.nab.icommerce.api.gateway.dto.response.ProductResponse;
import au.com.nab.icommerce.product.protobuf.PProduct;
import au.com.nab.icommerce.protobuf.mapper.ProtobufMapper;
import au.com.nab.icommerce.protobuf.mapper.ProtobufMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = ProtobufMapperConfig.class)
public interface ProductResponseMapper extends ProtobufMapper<ProductResponse, PProduct> {
    ProductResponseMapper INSTANCE = Mappers.getMapper(ProductResponseMapper.class);
}