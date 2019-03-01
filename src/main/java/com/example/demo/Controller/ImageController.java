package com.example.demo.Controller;

import com.example.demo.Model.DTO.ImageUploadDTO;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
public class ImageController {

    @Autowired
    UserRepository userRepository;

    public static final String IMAGE_DIR = "C://Users/Prod/Desktop/";

    @PostMapping("/images")
    public void uploadImage(@RequestBody ImageUploadDTO dto, HttpSession ses) throws IOException {
        User user = (User) ses.getAttribute("loggedUser");
        String base64 = dto.getFileStr();
        System.out.println(base64);
        byte[] bytes = Base64.getDecoder().decode(base64);
        String name = user.getUserId() + System.currentTimeMillis()+".png";
        File newImage = new File(IMAGE_DIR +name);
        FileOutputStream fos = new FileOutputStream(newImage);
        fos.write(bytes);
        user.setImageUrl(name);
        userRepository.save(user);
    }

    @GetMapping(value="/images/{name}", produces = "image/png")
    public byte[] downloadImage(@PathVariable("name") String imageName, HttpServletResponse resp) throws IOException {
        File newImage = new File(IMAGE_DIR +imageName);
        byte[] bytesArray = new byte[(int) newImage.length()];
        FileInputStream fis = new FileInputStream(newImage);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();
        return bytesArray;
    }

}
