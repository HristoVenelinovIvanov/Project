package com.example.demo.model.enums;

public enum Notification {

    DISCOUNT {
        @Override
        public String toString() {
            return "Hello, \nThere is a discount. Check the discounts here:" +
                    " \nhttp://localhost:1337/home/discount";
        }
    },

    BLACK_FRIDAY {
        @Override
        public String toString() {
            return "Hello, \nToday is black friday! Check the discounts here:" +
                    " \nhttp://localhost:1337/home/discount";
        }
    },

    CRAZY_DAYS {
        @Override
        public String toString() {
            return "Hello, \nThe upcoming days are beyond crazy. Check our crazy prices at:" +
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
