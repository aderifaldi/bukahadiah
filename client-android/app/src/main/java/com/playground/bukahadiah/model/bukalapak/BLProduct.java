package com.playground.bukahadiah.model.bukalapak;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by RadyaLabs PC on 17/05/2017.
 */

public class BLProduct implements Serializable {

    public class Product implements Serializable {

        public class Rating implements Serializable {
            public float average_rate;
            public int user_count;
        }

        public String seller_username, seller_name, seller_id, seller_avatar, seller_level,
                seller_level_badge_url, seller_delivery_time, seller_positive_feedback,
                seller_negative_feedback, seller_term_condition, id, url, name, city, province,
                weight, desc, category, condition;
        public String[] images, small_images, courier;
        public int sold_count, stock, category_id, view_count;
        public long price;
        public Rating rating;
        public Date updated_at;
    }

    public String status;
    public Product[] products;

}
