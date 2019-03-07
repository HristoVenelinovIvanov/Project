package com.example.demo.model.pojo;

        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import javax.persistence.*;
        import java.time.LocalDate;
        import java.time.temporal.ChronoUnit;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private long orderId;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "address")
    private String address;
    @Column(name = "quantity")
    private long quantity;
    @Column(name = "status")
    private String status = "Not shipped";
    //By default sets the shipping date 2 weeks ahead if required shipping date is not set.
    @Column(name = "required_date")
    private String requiredDate = LocalDate.now().plus(2, ChronoUnit.WEEKS).toString();
    @Column(name = "shipped_date")
    private String shippedDate = "Not given";

}
