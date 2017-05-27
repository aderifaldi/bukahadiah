package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by RadyaLabs PC on 15/05/2017.
 */

public class BHEvent extends ModelBase implements Serializable {

    public class EventData implements Serializable{
        public int event_id, user_id, gift_count;
        public String event_name, user_blid, event_description, event_photo, user_name, user_photo;
        public Date created_date;
    }

    public EventData[] data;

}
