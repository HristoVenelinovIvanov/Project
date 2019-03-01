package com.example.demo.Model.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "order_id")
    private long orderId;
    @Column(name = "product_id")
    private long productId;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "product_quantity")
    private int productQuantity;
    @Column(name = "status")
    private String status;
    @Column(name = "required_date")
    private String requiredDate;
    @Column(name = "shipped_date")
    private String shippedDate;
    private List<Product> products;

}
