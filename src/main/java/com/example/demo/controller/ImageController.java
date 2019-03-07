package com.example.demo.controller;

import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.exceptions.TechnoMarketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class ImageController extends BaseController{

    @Autowired
    private UserRepository userRepository;

    public String getPath(){
        String path = "C:" + File.separator + "Users" + File.separator +
                "Prod" + File.separator + "Desktop" + File.separator + "images" + File.separator;
        return path;
    }

    @PostMapping("/images")
    public void uploadImage(@RequestParam MultipartFile img, HttpSession ses) throws IOException, TechnoMarketException {

        validateAdminLogin(ses);
        User user = (User) ses.getAttribute("userLogged");
        System.out.println(user);
        byte[] bytes = img.getBytes();
        String name = user.getUserId() + System.currentTimeMillis()+".png";
        File newImage = new File(getPath() + name);
        FileOutputStream fos = new FileOutputStream(newImage);
        fos.write(bytes);
        fos.close();
        user.setImageUrl(name);
        userRepository.saveAndFlush(user);
    }

    @GetMapping(value="/images/{name}", produces = "image/png")
    public byte[] downloadImage(@PathVariable("name") String imageName) throws IOException {
        File newImage = new File(getPath() + imageName);
        byte[] bytesArray = new byte[(int) newImage.length()];
        FileInputStream fis = new FileInputStream(newImage);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();
        return bytesArray;
    }

}
