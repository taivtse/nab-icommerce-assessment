package au.com.nab.icommerce.api.gateway.mapper.response;

import au.com.nab.icommerce.api.gateway.dto.response.CustomerResponse;
import au.com.nab.icommerce.customer.protobuf.PCustomer;
import au.com.nab.icommerce.protobuf.mapper.ProtobufMapper;
import au.com.nab.icommerce.protobuf.mapper.ProtobufMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = ProtobufMapperConfig.class)
public interface CustomerResponseMapper extends ProtobufMapper<CustomerResponse, PCustomer> {
    CustomerResponseMapper INSTANCE = Mappers.getMapper(CustomerResponseMapper.class);
}