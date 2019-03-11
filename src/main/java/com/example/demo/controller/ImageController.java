package com.example.demo.controller;

import com.example.demo.model.pojo.ProductImage;
import com.example.demo.model.repository.ProductImageRepository;
import com.example.demo.utility.exceptions.TechnoMarketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class ImageController extends BaseController{

    @Autowired
    private ProductImageRepository productImageRepository;

    public String getPath(){
        String path = "/Users/theddy/Desktop/"; //"C:" + File.separator + "Users" + File.separator +
//                "Prod" + File.separator + "Desktop" + File.separator + "images" + File.separator;
        ///Users/theddy/Desktop
        return path;
    }

    //Beta version - User avatar(image)
//    @PostMapping("/user/images")
//    public void uploadUserImage(@RequestParam MultipartFile img, HttpSession ses) throws IOException, TechnoMarketException {
//
//        validateLogin(ses);
//        User user = (User) ses.getAttribute("userLogged");
//        System.out.println(user);
//        byte[] bytes = img.getBytes();
//        String name = getPath() + user.getUserId() + System.currentTimeMillis()+".png";
//        File newImage = new File(name);
//        FileOutputStream fos = new FileOutputStream(newImage);
//        fos.write(bytes);
//        fos.close();
//        user.setImageUrl(name);
//        userRepository.saveAndFlush(user);
//    }

    //Adds an image to DB and creates image to absolute path, returns image ID
    @RequestMapping(value = "/products/images", method = RequestMethod.POST)
    public void uploadProductImage(@RequestParam MultipartFile img, HttpServletResponse response, HttpSession ses) throws IOException, TechnoMarketException {

        validateAdminLogin(ses);

        byte[] bytes = img.getBytes();
        ProductImage p = new ProductImage();
        String name = getPath() + System.currentTimeMillis()+ ".png";
        p.setImageName(name);
        File newImage = new File(name);
        FileOutputStream fos = new FileOutputStream(newImage);
        fos.write(bytes);
        fos.close();
        productImageRepository.save(p);
        response.getWriter().append("The uploaded image has Id: " + p.getProductImageId());
    }

    //Shows image
    @GetMapping(value="/images/{name}", produces = "image/png")
    public byte[] downloadImage(@PathVariable("name") String imageName) throws IOException {

        File newImage = new File(getPath() + imageName);
        byte[] bytesArray = new byte[(int) newImage.length()];
        FileInputStream fis = new FileInputStream(newImage);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();
        return bytesArray;
    }

    //Shows image by productId
    @GetMapping(value = "/product/{productId}/image", produces = "image/png")
    public byte[] showImageByProductId(@PathVariable("productId") long productId) throws Exception {

        String productImageName = productImageRepository.getImageNameByProductId(productId);

        File newImage = new File(productImageName);
        byte[] bytesArray = new byte[(int) newImage.length()];
        FileInputStream fis = new FileInputStream(newImage);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();
        return bytesArray;

    }

}
