package com.example.demo.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "price")
    private Double price;
    @Column(name = "quantity")
    private Long quantity = 0L;

    //Quantity on order is the quantity that is *NOT* available
    //E.G Ordered by a user OR marked DAMAGED/NOT FOR SALE

    @Column(name = "quantity_on_order")
    private Long quantityOnOrder = 0L;
    @Column(name = "category_id")
    private Long categoryId = 1L;
    @Column(name = "discounted")
    private Double discounted;
    @Column(name = "brand")
    private String brand;
    @Column(name = "inches")
    private Double inches;
    @Column(name = "frequency")
    private Long frequency;
    @Column(name = "kw")
    private Long kw;
    @Column(name = "number_of_hobs")
    private Long numberOfHobs;
    @Column(name = "timer")
    private Long timer;
    @Column(name = "camera_pixels")
    private Double cameraPixels;
    @Column(name = "finger_print")
    private Long fingerPrint;
    @Column(name = "water_proof")
    private Long waterProof;
    @Column(name = "kg_capacity")
    private Long kgCapacity;
    @Column(name = "intelligent_wash")
    private Long intelligentWash;
    @Column(name = "inbuild_dryer")
    private Long inbuildDryer;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return productId == product.productId &&
                Double.compare(product.price, price) == 0 &&
                categoryId == product.categoryId &&
                Double.compare(product.discounted, discounted) == 0 &&
                Objects.equals(productName, product.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, price, categoryId, discounted);
    }

    @Override
    public String toString() {
        return "\n\n Product: " + productName + "\n Price: " + price + "\n";
    }

}

