package com.example.demo.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "product_id")
    private long productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "price")
    private double price;
    @Column(name = "quantity")
    private long quantity;

    //Quantity on order is the quantity that is *NOT* available
    //E.G Ordered by a user OR marked DAMAGED/NOT FOR SALE

    @Column(name = "quantity_on_order")
    private long quantityOnOrder;
    @Column(name = "category_id")
    private long categoryId;
    @Column(name = "discounted")
    private int discounted;
    @Column(name = "characteristics")
    private String characteristics;
    @Column(name = "product_image")
    private String productImage;
    @Column(name = "inches")
    private long inches;


}

