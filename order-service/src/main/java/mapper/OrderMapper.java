package mapper;

import dto.OrderRequestDto;
import dto.OrderResponseDto;
import entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toEntity(OrderRequestDto requestDto){
        Order order = new Order();
        order.setSkuCode(requestDto.getSkuCode());
        order.setQuantity(requestDto.getQuantity());
        order.setPrice(requestDto.getPrice());
        return order;
    }

    public OrderResponseDto toDto(Order order){
        return new OrderResponseDto(
                order.getOrderNo(),
                order.getOrderStatus()
        );
    }
}
