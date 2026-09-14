package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    @NotBlank
    private String skuCode;
    @NotNull
    @Min(value = 1, message = "Quantity at least 1")
    private Integer quantity;
    @NotNull(message = "Price is mandatory")
    @DecimalMin("0.01")
    private BigDecimal price;
}
