package com.example.demo.Model.POJO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE, setterVisibility=JsonAutoDetect.Visibility.NONE, creatorVisibility=JsonAutoDetect.Visibility.NONE)
public class Product {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonProperty(value = "product_id")
    private long productId;
    @JsonProperty(value = "product_name")
    private String productName;
    private double price;
    private long quantity;
    //Quantity on order is the quantity that is *NOT* available
    //E.G Ordered by a user OR marked DAMAGED/NOT FOR SALE
    @JsonProperty(value = "quantity_on_order")
    private Long quantityOnOrder;
    @JsonProperty(value = ("category_id"))
    private int categoryId;
    @JsonProperty(value = "a_category_id")
    private Integer aCategoryId;
    @JsonProperty(value = "b_category_id")
    private Integer bCategoryId;
    @JsonProperty(value = "c_category_id")
    private Integer cCategoryId;
    private Integer discounted;
    private String characteristics;
    @JsonProperty(value ="product_image")
    private String productImage;


}


/*
{
    "productName" : "LENOVO LEGION Y530-15ICH 81FV0196BM",
	"price": 2299.00,
	"quantity": 20,
    "categoryId" : 2,
    "discounted" : 0,
    "characteristics" : "DISPLAY: 15.6 FHD IPS AG 250N 60 (1920 X 1080)CPU: INTEL® I7-8750H RAM: 16G(1X16GBDDR4 2666) HDD: 1TB 7MM 5400RPM SSD: 128G M.2 PCIE 2242 VIDEO: GEFORCE GTX 1050, TI 4 GB PORTS: CARD READER 4-IN-1, 3X USB 3.1, 1X USB-C, 1X HDMI, 3.5 MM КОМБО ЖАК ЗА МИКРОФОН И СЛУШАЛКИ OS: FREE-DOS БАТЕРИЯ: 3CELL 52.5WHV ЦВЯТ: ЧЕРЕН ТЕГЛО: 2.30 KГ"
}
 */