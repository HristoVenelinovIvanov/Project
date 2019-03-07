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
    private double discounted;
    @Column(name = "product_image")
    private String productImage;
    @Column(name = "brand")
    private String brand;
    @Column(name = "inches")
    private double inches;
    @Column(name = "frequency")
    private long frequency;
    @Column(name = "kw")
    private long kw;
    @Column(name = "number_of_hobs")
    private long numberOfHobs;
    @Column(name = "timer")
    private long timer;
    @Column(name = "camera_pixels")
    private double cameraPixels;
    @Column(name = "finger_print")
    private long fingerPrint;
    @Column(name = "water_proof")
    private long waterProof;
    @Column(name = "kg_capacity")
    private long kgCapacity;
    @Column(name = "intelligent_wash")
    private long intelligentWash;
    @Column(name = "inbuild_dryer")
    private long inbuildDryer;








}

