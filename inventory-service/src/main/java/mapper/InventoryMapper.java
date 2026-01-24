package mapper;

import dto.InventoryRequest;
import dto.InventoryResponse;
import entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {
    public Inventory toEntity(InventoryRequest dto){
        Inventory inventory = new Inventory();
        inventory.setSkuCode(dto.getSkuCode());
        inventory.setQuantity(dto.getQuantity());
        return inventory;
    }

    public InventoryResponse toDto(Inventory inventory){
        return new InventoryResponse(
                inventory.getSkuCode(),
                inventory.getQuantity()>0,
                inventory.getQuantity()
        );
    }
}
