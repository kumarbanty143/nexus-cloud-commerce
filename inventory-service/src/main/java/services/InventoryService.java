package services;

import dto.InventoryRequest;
import dto.InventoryResponse;
import entity.Inventory;
import event.OrderPlacedEvent;
import exception.InventoryNotFound;
import lombok.RequiredArgsConstructor;
import mapper.InventoryMapper;
import org.springframework.stereotype.Service;
import repository.InventoryRepository;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    public InventoryResponse checkInventory(String skuCode){
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode).orElseThrow(()->new InventoryNotFound(skuCode));
        return inventoryMapper.toDto(inventory);
    }
    public void addInventory(InventoryRequest request){
        Inventory inventory = inventoryMapper.toEntity(request);
        inventoryRepository.save(inventory);
    }

    public void updateStock(OrderPlacedEvent event){
        Inventory inventory = inventoryRepository.findBySkuCode(event.getSkuCode())
                .orElseThrow(()-> new InventoryNotFound(event.getSkuCode()));
        Integer availableQuantity = inventory.getQuantity();
        Integer orderQuantity = event.getQuantity();
        if(availableQuantity <orderQuantity){
            throw new RuntimeException("Insufficient stock for this sku code");
        }
        inventory.setQuantity(availableQuantity -orderQuantity);
        inventoryRepository.save(inventory);
    }
}
