package com.playground.bukahadiah.model.bukalapak;

import java.io.Serializable;

/**
 * Created by RadyaLabs PC on 15/05/2017.
 */

public class BLUser implements Serializable {

    public class UserData implements Serializable {

        public class Bank implements Serializable {
            public String name, number;
        }

        public String name, email, phone, avatar, avatar_id;
        public Bank bank;
    }

    public String status, message;
    public UserData user;

}
