package io.fepay.backend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    private String id;

    @Field("festival_id")
    private String festivalId;

    private String sender;

    private String recipient;

    private int amount;

    private LocalDateTime createdAt;

    private LocalDateTime matchedAt;

    private boolean isClosed;
}
