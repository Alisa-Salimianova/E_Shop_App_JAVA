package io.github.alisa_salimianova.eshop.mapper;

import io.github.alisa_salimianova.eshop.dto.response.OrderResponse;
import io.github.alisa_salimianova.eshop.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    OrderResponse toResponse(Order order);
}