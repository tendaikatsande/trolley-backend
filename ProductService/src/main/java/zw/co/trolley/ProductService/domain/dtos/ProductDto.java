package zw.co.trolley.ProductService.domain.dtos;

import lombok.*;
import zw.co.trolley.ProductService.domain.models.Category;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
    private Category category;

}
