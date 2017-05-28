package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aderifaldi on 28-May-17.
 */

public class BHInvoiceDetail extends ModelBase implements Serializable {

    public class InvoiceData implements Serializable{

        public class Invoice implements Serializable {

            public class Buyer implements Serializable {
                public int id;
                public String name;
            }

            public class Consignee implements Serializable {
                public String name, phone, address, area, city, province, post_code;
            }

            public class Transaction implements Serializable{
                public class Seller implements Serializable {
                    public String name;
                }

                public class Product implements Serializable {
                    public String id;
                    public String name;
                    public String[] images;
                }

                public Seller seller;
                public Product[] products;

            }

            public int id, uniq_code;
            public String state, invoice_id, payment_method_name;
            public Date created_at, pay_before;
            public long amount, shipping_fee, total_amount, coded_amount;
            public Buyer buyer;
            public Transaction[] transactions;
            public Consignee consignee;

        }

        public Invoice invoice;

    }

    public InvoiceData data;

}
