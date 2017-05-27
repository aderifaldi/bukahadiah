package com.playground.bukahadiah.model.bukalapak;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aderifaldi on 21-May-17.
 */

public class BLProductReview implements Serializable {

    public class Review implements Serializable {
        public String sender_name, body;
        public Date updated_at;
    }

    public String status;
    public Review[] reviews;

}
