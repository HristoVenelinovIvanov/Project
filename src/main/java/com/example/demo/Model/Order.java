package com.example.demo.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Order {

    private long orderId;
    private long productId;
    private long userId;
    private int productQuantity;
    private String status;
    private String requiredDate;
    private String shippedDate;

}
