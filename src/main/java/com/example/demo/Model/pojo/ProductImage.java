package com.example.demo.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "products_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "product_image_id")
    private long productsImageId;
    @Column(name = "image_name")
    private String imageName;
    @Column(name = "product_id")
    private String productId;

}
