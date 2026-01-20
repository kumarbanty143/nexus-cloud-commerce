package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {
    private String skuCode;
    private boolean inStock;
    private Integer availableQuantity;
}
