package com.example.demo.model.enums;

import org.springframework.beans.factory.annotation.Autowired;

public enum Notification {

    DISCOUNT{
        @Override
        public String toString(){
            return discount(DISCOUNT.name());
        };
    },

    BLACK_FRIDAY{
        @Override
        public String toString(){
            return discount(BLACK_FRIDAY.name());
        };
    },

    CYBER_MONDAY{
        @Override
        public String toString(){
            return discount(CYBER_MONDAY.name());
        };
    },

    CRAZY_DAYS{
        @Override
        public String toString(){
            return discount(CRAZY_DAYS.name());
        };
    };

    protected String discount(String discount){
        return "There is a new "+ discount +"! \nCheck it here: " + "http://localhost:1337/products";
    }

}
