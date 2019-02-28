package com.example.demo.Model.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_favorites")
public class UserFavorites {

    @Id
    @Column(name = "user_id")
    private long userId;
    @Column(name = "product_id")
    private long productId;
}
