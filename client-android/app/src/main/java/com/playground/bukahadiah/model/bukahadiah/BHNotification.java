package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aderifaldi on 27-May-17.
 */

public class BHNotification extends ModelBase implements Serializable {

    public class NotificationData implements Serializable {
        public int id, gift_item_id;
        public String user_id, user_name, user_photo, title, message;
        public Date create_date;
    }

    public NotificationData[] data;

}
