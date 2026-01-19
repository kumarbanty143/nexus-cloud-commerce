package controller;

import dto.InventoryRequest;
import dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.InventoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> isInStock(@PathVariable String skuCode){
        return ResponseEntity.ok(inventoryService.checkInventory(skuCode));
    }

    @PostMapping
    public ResponseEntity<String> addInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.addInventory(inventoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Inventory added Successfully");
    }
}
