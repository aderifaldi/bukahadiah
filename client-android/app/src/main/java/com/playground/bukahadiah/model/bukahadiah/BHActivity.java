package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by RadyaLabs PC on 24/05/2017.
 */

public class BHActivity extends ModelBase implements Serializable {

    public class ActivityData implements Serializable {
        public int id, type, activity_item_id;
        public String user_id, user_name, title, desc, pic, user_photo, product_blid;
        public Date created_date;
    }

    public ActivityData[] data;

}
