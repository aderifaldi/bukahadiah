package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;

/**
 * Created by aderifaldi on 28-May-17.
 */

public class BHInvoiceList extends ModelBase implements Serializable {

    public class InvoiceListData implements Serializable {
        public int id, item_id;
        public String user_id, invoice_id, invoice_state, product_blid, item_name, item_photo, status;
        public boolean is_completed;
        public long total_amount;

    }

    public InvoiceListData[] data;

}
