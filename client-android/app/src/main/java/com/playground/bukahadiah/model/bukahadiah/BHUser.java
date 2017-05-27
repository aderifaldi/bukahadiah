package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by RadyaLabs PC on 15/05/2017.
 */

public class BHUser extends ModelBase implements Serializable {

    public class UserData implements Serializable{
        public String user_name, user_photo, user_blid, user_screename;
        public int user_id, followers_count, following_count;
    }

    public UserData data;

}
