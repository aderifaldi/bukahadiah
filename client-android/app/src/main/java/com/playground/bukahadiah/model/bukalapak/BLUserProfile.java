package com.playground.bukahadiah.model.bukalapak;

import java.io.Serializable;

/**
 * Created by aderifaldi on 21-May-17.
 */

public class BLUserProfile implements Serializable {

    public class User implements Serializable {
        public int id;
        public String name, email, phone, avatar, gender, username;
        public boolean confirmed;
    }

    public String status;
    public User user;

}
