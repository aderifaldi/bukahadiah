package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aderifaldi on 21-May-17.
 */

public class BHMemberList extends ModelBase implements Serializable {

    public class MemberData implements Serializable {
        public int user_id, followers_count, friends_count;
        public String user_name, user_photo, user_desc, user_blid, user_screename;
        public Date registered_date;
        public boolean is_following, is_follower;
    }

    public MemberData[] data;

}
