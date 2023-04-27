package com.pizzurg.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="deliveries_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name= "order_id")
    private Order order;

    @Column(name = "receiver_name")
    private String receiverName;

    private String address;

    @Column(name = "house_number")
    private String number;

    private String complement;

    private String district;

    @Column(name = "zip_code")
    private String zipCode;

    private String city;

    private String state;

}
