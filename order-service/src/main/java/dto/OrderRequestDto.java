package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private String skuCode;
    @Min(value = 1, message = "Quantity at least 1")
    private Integer quantity;
    @NotNull(message = "Price is mandatory")
    private BigDecimal price;
}
