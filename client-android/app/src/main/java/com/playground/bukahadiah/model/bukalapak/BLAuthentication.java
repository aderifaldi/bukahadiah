package com.playground.bukahadiah.model.bukalapak;

import java.io.Serializable;

/**
 * Created by aderifaldi on 15-May-17.
 */

public class BLAuthentication implements Serializable {

    public String status, message, user_name, token, email, confirmed_phone, omnikey;
    public boolean confirmed;
    public int user_id;

}
