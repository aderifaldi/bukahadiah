package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;

/**
 * Created by RadyaLabs PC on 15/05/2017.
 */

public class BHUserDetail extends ModelBase implements Serializable {

    public class UserData implements Serializable{
        public String user_name, user_photo, user_blid, user_screename;
        public int user_id, followers_count, following_count;
        public boolean is_following, is_follower;
    }

    public UserData[] data;

}
