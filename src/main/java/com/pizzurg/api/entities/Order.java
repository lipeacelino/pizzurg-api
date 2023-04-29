package com.pizzurg.api.entities;

import com.pizzurg.api.enums.MethodPayment;
import com.pizzurg.api.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "method_payment")
    private MethodPayment methodPayment;

    private BigDecimal amount;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private DeliveryData deliveryData;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    private void configureCreatedDate() {
        this.createdDate = LocalDateTime.now();
    }

}
