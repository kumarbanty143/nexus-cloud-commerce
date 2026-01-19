package exception;

public class InventoryNotFound extends RuntimeException{
    public InventoryNotFound(String skuCode){
        super("Inventory not found for skuCode: "+ skuCode);
    }
}
