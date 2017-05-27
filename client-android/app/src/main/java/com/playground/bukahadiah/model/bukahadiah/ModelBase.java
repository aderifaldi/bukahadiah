package com.playground.bukahadiah.model.bukahadiah;

import java.io.Serializable;

/**
 * Created by aderifaldi on 04/01/2017.
 */

public class ModelBase implements Serializable {

    public class Alerts implements Serializable {
        public int code;
        public String message;
    }

    protected boolean error;
    protected Alerts alerts;

    public boolean isError() {
        return error;
    }

    public Alerts getAlerts() {
        return alerts;
    }

}
