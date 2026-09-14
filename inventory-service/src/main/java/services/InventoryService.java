package services;

import dto.InventoryRequest;
import dto.InventoryResponse;
import entity.Inventory;
import entity.ProcessedOrder;
import event.OrderPlacedEvent;
import exception.InventoryNotFound;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mapper.InventoryMapper;
import org.springframework.stereotype.Service;
import repository.InventoryRepository;
import repository.ProcessedOrderRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProcessedOrderRepository processedOrderRepository;
    public InventoryResponse checkInventory(String skuCode){
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode).orElseThrow(()->new InventoryNotFound(skuCode));
        return inventoryMapper.toDto(inventory);
    }
    public void addInventory(InventoryRequest request){
        Inventory inventory = inventoryMapper.toEntity(request);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void updateStock(OrderPlacedEvent event){
        if (event.getOrderId() == null || event.getOrderId().isBlank()
                || event.getSkuCode() == null || event.getSkuCode().isBlank()
                || event.getQuantity() == null || event.getQuantity() < 1) {
            throw new IllegalArgumentException("Order ID, SKU and positive quantity are required");
        }
        if(processedOrderRepository.existsByOrderId(event.getOrderId())){
            return;
        }
        Inventory inventory = inventoryRepository.findBySkuCode(event.getSkuCode())
                .orElseThrow(()-> new InventoryNotFound(event.getSkuCode()));
        Integer availableQuantity = inventory.getQuantity();
        Integer orderQuantity = event.getQuantity();
        if(availableQuantity <orderQuantity){
            throw new RuntimeException("Insufficient stock for this sku code");
        }
        inventory.setQuantity(availableQuantity -orderQuantity);
        inventoryRepository.save(inventory);

        ProcessedOrder processedOrder = new ProcessedOrder();
        processedOrder.setOrderId(event.getOrderId());
        processedOrder.setProcessedAt(LocalDateTime.now());
        processedOrderRepository.save(processedOrder);

    }
}
