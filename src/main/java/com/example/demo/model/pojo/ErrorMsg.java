package com.example.demo.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class ErrorMsg {

    private String msg;
    private int status;
    private LocalDateTime localDateTime;


}
