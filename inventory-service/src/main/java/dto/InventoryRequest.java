package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    @NotBlank(message = "skuCode must not be blank")
        private String skuCode;
    @Min(value = 0, message = "Minimum quantity must be 0")
        private Integer quantity;
}
