package au.com.nab.icommerce.protobuf.mapper;

import au.com.nab.icommerce.common.protobuf.PSorting;
import au.com.nab.icommerce.protobuf.domain.Sorting;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = DirectionMapper.class)
public interface SortingMapper extends ProtobufMapper<Sorting, PSorting> {
    SortingMapper INSTANCE = Mappers.getMapper(SortingMapper.class);
}
