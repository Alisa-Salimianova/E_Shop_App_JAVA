package io.github.alisa_salimianova.eshop.mapper;

import io.github.alisa_salimianova.eshop.dto.request.CreateProductRequest;
import io.github.alisa_salimianova.eshop.dto.request.UpdateProductRequest;
import io.github.alisa_salimianova.eshop.dto.response.ProductResponse;
import io.github.alisa_salimianova.eshop.model.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", constant = "0.0")
    @Mapping(target = "ratingCount", constant = "0")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Product toEntity(CreateProductRequest request);

    ProductResponse toResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Product product, UpdateProductRequest request);
}