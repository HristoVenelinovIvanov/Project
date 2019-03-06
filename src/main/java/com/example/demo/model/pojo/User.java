package com.example.demo.model.pojo;

import com.example.demo.model.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    public static final int USER_ROLE_ADMINISTRATOR = 1, IS_USER_VERIFIED = 1, IS_USER_SUBSCRIBED = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long userId;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "user_role_id")
    private long userRoleId = 1;
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "image")
    private String imageUrl;
    @Column(name = "verified")
    private int verified = 0;
    @Column(name = "subscribed")
    private int subscribed = 0;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> favorites = new ArrayList<>();


}



