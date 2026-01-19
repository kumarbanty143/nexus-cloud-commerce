package services;

import dto.InventoryRequest;
import dto.InventoryResponse;
import entity.Inventory;
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
}
