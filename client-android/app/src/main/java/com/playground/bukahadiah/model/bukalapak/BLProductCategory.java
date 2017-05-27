package com.playground.bukahadiah.model.bukalapak;

import java.io.Serializable;

/**
 * Created by RadyaLabs PC on 17/05/2017.
 */

public class BLProductCategory implements Serializable{

    public class Categories implements Serializable {
        public int id;
        public String name;
    }

    public String status;
    public Categories[] categories;

}
