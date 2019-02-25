package com.example.demo.Model.Enums;

public enum Gender {

    MALE() {
        @Override
        public String toString() {
            return "MALE";
        }
    },

    FEMALE() {
        @Override
        public String toString() {
            return "FEMALE";
        }
    }
}
