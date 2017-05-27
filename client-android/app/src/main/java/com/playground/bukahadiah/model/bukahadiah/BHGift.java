package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;

/**
 * Created by RadyaLabs PC on 15/05/2017.
 */

public class BHGift extends ModelBase implements Serializable {

    public class GiftData implements Serializable{
        public int item_id, event_id;
        public String item_name, item_description, item_blid;
        public boolean is_completed;
    }

    public GiftData data;

}
