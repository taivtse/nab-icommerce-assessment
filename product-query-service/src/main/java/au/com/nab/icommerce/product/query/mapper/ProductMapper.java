package au.com.nab.icommerce.product.query.mapper;

import au.com.nab.icommerce.product.protobuf.PProduct;
import au.com.nab.icommerce.product.query.domain.Product;
import au.com.nab.icommerce.protobuf.mapper.ProtobufMapper;
import au.com.nab.icommerce.protobuf.mapper.ProtobufMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = ProtobufMapperConfig.class)
public interface ProductMapper extends ProtobufMapper<Product, PProduct> {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
}
