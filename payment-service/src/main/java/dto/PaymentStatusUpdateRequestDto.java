package dto;

import enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusUpdateRequestDto {
    @NotNull
    private PaymentStatus paymentStatus;
}
