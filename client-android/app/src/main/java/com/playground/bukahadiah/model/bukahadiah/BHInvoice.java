package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aderifaldi on 28-May-17.
 */

public class BHInvoice extends ModelBase implements Serializable {

    public class InvoiceData implements Serializable {
        public class Invoice implements Serializable {
            public int id, uniq_code;
            public Date updated_at, pay_before, payment_chosen_at;
            public String invoice_id, payment_method_name;
            public long amount, shipping_fee, total_amount, coded_amount;
        }

        public class Buyer implements Serializable {
            public int id;
            public String name, username, email;
        }

        public class Consignee implements Serializable {
            public String name, phone, address, area, city, province, post_code;
        }

        public class Transaction implements Serializable {
            public class Seller implements Serializable {
                public int id;
                public String name, username;
            }

            public class Products implements Serializable {
                public int id;
                public String url, name;
                public String[] images;
                public long price;
                public  int order_quantity;

            }

            public Seller seller;
            public Products[] products;

        }

        public Invoice invoice;
        public Buyer buyer;
        public Consignee consignee;
        public Transaction[] transactions;
        public String product_description;
    }

    public InvoiceData data;

}
