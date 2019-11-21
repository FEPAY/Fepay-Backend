package io.fepay.backend.domain.payload;

import io.fepay.backend.domain.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentForm {

    private int amount;

    public Payment toPayment() {
        return new Payment(new ObjectId().toString(), null, null, null, amount, LocalDateTime.now(), null, false);
    }

}
