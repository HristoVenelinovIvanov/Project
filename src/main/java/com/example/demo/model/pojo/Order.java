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

    public static final String IS_ORDER_CONFIRMED = "Order is not confirmed yet";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private long orderId;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "address")
    private String address;
    @Column(name = "quantity")
    private long quantity = 1;
    @Column(name = "status")
    private String status = "Not shipped";

    //By default sets the shipping date 4 days ahead if required shipping date is not set.
    @Column(name = "required_date")
    private String requiredDate = LocalDate.now().plus(4, ChronoUnit.DAYS).toString();
    @Column(name = "shipped_date")
    private String shippedDate = "Not given";

    @Override
    public String toString() {
        return
                "\nOrder ID = " + orderId +
                "\nAddress = '" + address + '\'' +
                "\nQuantity = " + quantity +
                "\nStatus = '" + status + '\'' +
                "\nDate expected = '" + requiredDate + '\'';
    }

    public Order(String address,long userId, long quantity, String status, String requiredDate) {
        this.address = address;
        this.userId = userId;
        this.quantity = quantity;
        this.status = status;
        this.requiredDate = requiredDate;
    }

    public Order(long userId, String address, long quantity) {
        this.userId = userId;
        this.address = address;
        this.quantity = quantity;
    }
}
