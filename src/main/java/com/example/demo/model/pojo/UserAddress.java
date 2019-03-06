package com.example.demo.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_addresses")
public class UserAddress {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "user_details_id")
    private long id;

    @Column(name = "user_id")
    private long userId;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "order_id")
    private long orderId;
    @Column(name = "city_name")
    private String cityName;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "street_name")
    private String streetName;
    @Column(name = "street_number")
    private String streetNumber;
    @Column(name = "block")
    private String block;
    @Column(name = "entrance")
    private String entrance;
    @Column(name = "floor")
    private String floor;
    @Column(name = "apartment")
    private String apartment;



    @Override
    public String toString() {
        return "Shipping Address{" +
                "Full Name ='" + fullName + '\'' +
                ", Telephone ='" + telephone + '\'' +
                ", City ='" + cityName + '\'' +
                ", Postal Code ='" + postalCode + '\'' +
                ", Street Name ='" + streetName + '\'' +
                ", Street Number ='" + streetNumber + '\'' +
                ", Block ='" + block + '\'' +
                ", Entrance ='" + entrance + '\'' +
                ", Floor ='" + floor + '\'' +
                ", Apartment ='" + apartment + '\'' +
                '}';
    }
}
