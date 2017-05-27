package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;

/**
 * Created by RadyaLabs PC on 24/05/2017.
 */

public class BHGiftItem extends ModelBase implements Serializable {

    public class GiftItemData implements Serializable {
        public int item_id, event_id;
        public String item_name, item_desc, item_blid, item_bl_params, item_pic;
        public boolean is_completed;
    }

    public GiftItemData[] data;

}
