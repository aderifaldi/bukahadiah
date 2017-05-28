package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;

/**
 * Created by aderifaldi on 28-May-17.
 */

public class BHBuy extends ModelBase implements Serializable {

    public class BuyData implements Serializable {
        public String product_name, seller_id, seller_name;
        public int bukahadiah_cart_id, cart_id;
        public long amount;
        public String[] courier;

    }

    public BuyData data;

}
