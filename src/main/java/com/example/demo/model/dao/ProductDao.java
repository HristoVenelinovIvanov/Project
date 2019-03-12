package com.example.demo.model.dao;

import com.example.demo.model.pojo.Product;
import com.example.demo.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductRepository productRepository;

    public static final int DELETE_COMMA = 1;
    public static final int DELETE_WHERE_CLAUSE = 5;

    public boolean productExists(long productId) {

        String sql = "SELECT COUNT(*) FROM products WHERE product_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {productId}, long.class) > 0;
    }

    public boolean productExists(String productName) {

        String sql = "SELECT COUNT(*) FROM products WHERE product_name LIKE LOWER(?)";

        return jdbcTemplate.queryForObject(sql, new Object[] {"%" +productName + "%"}, long.class) > 0;
    }

    public int checkQuantity(long productId) {

        String sql = "SELECT quantity FROM products WHERE product_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {productId}, int.class);
    }


    public void orderDecreaseQuantity(long quantity, Product product) {

            product.setQuantity(product.getQuantity() - quantity);
            product.setQuantityOnOrder(product.getQuantityOnOrder() + quantity);
            productRepository.saveAndFlush(product);

    }


    public List<Map<String, Object>> filterProducts(String productName, String price, String discounted,
                                                    String brand, String inches, String frequency, String kw,
                                                    String numberOfHobs, String timer, String cameraPixels,
                                                    String fingerPrint, String waterProof, String kgCapacity,
                                                    String intelligentWash, String inbuildDryer, String lowerThan, String higherThan){

        StringBuffer select = new StringBuffer("SELECT *  ");
        StringBuffer from = new StringBuffer("FROM test1339.products ");
        StringBuffer where = new StringBuffer("WHERE");

        if (productName != null && !productName.isEmpty()) {
            where.append(" product_name =").append("'").append(productName).append("' AND ");
        }
        if (price != null && !price.isEmpty()) {
            where.append(" price =").append(price).append(" AND ");
        }
        if (lowerThan != null && !lowerThan.isEmpty()) {
            where.append(" price <").append(lowerThan).append(" AND ");
        }
        if (higherThan != null && !higherThan.isEmpty()) {
            where.append(" price >").append(higherThan).append(" AND ");
        }
        if (discounted != null && !discounted.isEmpty()) {
//            select.append("discounted ,");
            where.append(" discounted =").append("'").append(discounted).append("' AND ");
        }
        if (brand != null && !brand.isEmpty()) {
//            select.append("brand ,");
            where.append(" brand =").append("'").append(brand).append("' AND ");
        }
        if (inches != null && !inches.isEmpty()) {
//            select.append("inches ,");
            where.append(" inches =").append(inches).append(" AND ");
        }
        if (frequency != null && !frequency.isEmpty()) {
//            select.append("frequency ,");
            where.append(" frequency =").append(frequency).append(" AND ");
        }
        if (kw != null && !kw.isEmpty()) {
//            select.append("kw ,");
            where.append(" kw =").append(kw).append(" AND ");
        }
        if (numberOfHobs != null && !numberOfHobs.isEmpty()) {
//            select.append("number_of_hobs ,");
            where.append(" number_of_hobs =").append(numberOfHobs).append(" AND ");
        }
        if (timer != null && !timer.isEmpty()) {
//            select.append("timer ,");
            where.append(" timer =").append(timer).append(" AND ");
        }
        if (cameraPixels != null && !cameraPixels.isEmpty()) {
//            select.append("camera_pixels ,");
            where.append(" camera_pixels =").append(cameraPixels).append(" AND ");
        }
        if (fingerPrint != null && !fingerPrint.isEmpty()) {
//            select.append("finger_print ,");
            where.append(" finger_print =").append(fingerPrint).append(" AND ");
        }
        if (waterProof != null && !waterProof.isEmpty()) {
//            select.append("water_proof ,");
            where.append(" water_proof =").append(waterProof).append(" AND ");
        }
        if (kgCapacity != null && !kgCapacity.isEmpty()) {
//            select.append("kg_capacity ,");
            where.append(" kg_capacity =").append(kgCapacity).append(" AND ");
        }
        if (intelligentWash != null && !intelligentWash.isEmpty()) {
//            select.append("intelligent_wash ,");
            where.append(" intelligent_wash =").append(intelligentWash).append(" AND ");
        }
        if (inbuildDryer != null && !inbuildDryer.isEmpty()) {
            //select.append("inbuild_dryer ,");
            where.append(" inbuild_dryer =").append(inbuildDryer).append(" AND ");
        }

        StringBuffer sql = new StringBuffer();
        select.setLength(select.length() - DELETE_COMMA);
        where.setLength(where.length() - DELETE_WHERE_CLAUSE);
        sql.append(select).append(from).append(where);
        System.out.println(sql);
        return jdbcTemplate.queryForList(sql.toString());

    }

//    public String filterProducts(String productName, String price, String discounted,
//                                 String brand, String inches, String frequency, String kw,
//                                 String numberOfHobs, String timer, String cameraPixels,
//                                 String fingerPrint, String waterProof, String kgCapacity,
//                                 String intelligentWash, String inbuildDryer){
//
//        StringBuffer select = new StringBuffer("SELECT price ,product_name ,");
//        StringBuffer from = new StringBuffer("FROM test1339.products ");
//        StringBuffer where = new StringBuffer("WHERE");
//
//        if (productName != null && !productName.isEmpty()) {
//            where.append(" product_name =").append("'").append(productName).append("' AND ");
//        }
//        if (price != null && !price.isEmpty()) {
//            where.append(" price =").append(price).append(" AND ");
//        }
//        if (discounted != null && !discounted.isEmpty()) {
//            select.append("discounted ,");
//            where.append(" discounted =").append("'").append(discounted).append("' AND ");
//        }
//        if (brand != null && !brand.isEmpty()) {
//            select.append("brand ,");
//            where.append(" brand =").append("'").append(brand).append("' AND ");
//        }
//        if (inches != null && !inches.isEmpty()) {
//            select.append("inches ,");
//            where.append(" inches =").append(inches).append(" AND ");
//        }
//        if (frequency != null && !frequency.isEmpty()) {
//            select.append("frequency ,");
//            where.append(" frequency =").append(frequency).append(" AND ");
//        }
//        if (kw != null && !kw.isEmpty()) {
//            select.append("kw ,");
//            where.append(" kw =").append(kw).append(" AND ");
//        }
//        if (numberOfHobs != null && !numberOfHobs.isEmpty()) {
//            select.append("number_of_hobs ,");
//            where.append(" number_of_hobs =").append(numberOfHobs).append(" AND ");
//        }
//        if (timer != null && !timer.isEmpty()) {
//            select.append("timer ,");
//            where.append(" timer =").append(timer).append(" AND ");
//        }
//        if (cameraPixels != null && !cameraPixels.isEmpty()) {
//            select.append("camera_pixels ,");
//            where.append(" camera_pixels =").append(cameraPixels).append(" AND ");
//        }
//        if (fingerPrint != null && !fingerPrint.isEmpty()) {
//            select.append("finger_print ,");
//            where.append(" finger_print =").append(fingerPrint).append(" AND ");
//        }
//        if (waterProof != null && !waterProof.isEmpty()) {
//            select.append("water_proof ,");
//            where.append(" water_proof =").append(waterProof).append(" AND ");
//        }
//        if (kgCapacity != null && !kgCapacity.isEmpty()) {
//            select.append("kg_capacity ,");
//            where.append(" kg_capacity =").append(kgCapacity).append(" AND ");
//        }
//        if (intelligentWash != null && !intelligentWash.isEmpty()) {
//            select.append("intelligent_wash ,");
//            where.append(" intelligent_wash =").append(intelligentWash).append(" AND ");
//        }
//        if (inbuildDryer != null && !inbuildDryer.isEmpty()) {
//            select.append("inbuild_dryer ,");
//            where.append(" inbuild_dryer =").append(inbuildDryer).append(" AND ");
//        }
//
//        StringBuffer sql = new StringBuffer();
//        select.setLength(select.length() - DELETE_COMMA);
//        where.setLength(where.length() - DELETE_WHERE_CLAUSE);
//        sql.append(select).append(from).append(where);
//        System.out.println(sql);
//        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
//        System.out.println(list.toString());
//
//        return sql.toString();
//
//    }




}
