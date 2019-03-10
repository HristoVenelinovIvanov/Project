package com.example.demo.model.enums;

public enum Notification {

    DISCOUNT {
        @Override
        public String toString() {
            return "Hello, \nThere is a discount. Check it here:" +
                    " \nhttp://localhost:1337/home/discount";
        }
    },

    BLACK_FRIDAY {
        @Override
        public String toString() {
            return "Hello, \nNow is black friday. Check it here:" +
                    " \nhttp://localhost:1337/home/discount";
        }
    },

    CRAZY_DAYS {
        @Override
        public String toString() {
            return "Hello, \nNow are crazy days. Check it here:" +
                    " \nhttp://localhost:1337/home/discount";
        }
    },

    CYBER_MONDAY {
        @Override
        public String toString() {
            return "Hello, \nAfter black friday comes Cyber Monday! Check it here:" +
                    " \nhttp://localhost:1337/home/discount";
        }
    }
}
