package com.example.demo.model.pojo;

        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import javax.persistence.*;
        import java.time.LocalDate;
        import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "order_id")
    private long orderId;
    @Column(name = "user_id")
    private long userId;
    @JoinTable(name = "user_addresses",
            joinColumns = @JoinColumn(name = "user_details_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private UserAddress userAddress;
    @Column(name = "product_id")
    private long productId;
    @Column(name = "product_quantity")
    private int productQuantity;
    @Column(name = "status")
    private String status = "Not shipped";
    //By default sets the shipping date 2 weeks ahead if required shipping date is not set.
    @Column(name = "required_date")
    private String requiredDate = LocalDate.now().plus(2, ChronoUnit.WEEKS).toString();
    @Column(name = "shipped_date")
    private String shippedDate;



}
