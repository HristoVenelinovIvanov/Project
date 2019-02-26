package com.example.demo.Model.Repository;

<<<<<<< HEAD
import com.example.demo.Model.Product;
=======
import com.example.demo.Model.POJO.Product;
>>>>>>> 7813b49e3a3cf4022bbdf69deec18b401cd3895e
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


}
