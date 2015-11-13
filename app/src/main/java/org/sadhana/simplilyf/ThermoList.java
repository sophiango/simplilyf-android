package org.sadhana.simplilyf;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sophiango on 11/13/15.
 */
public class ThermoList implements Serializable {
    public List<NestData> getThermoList() {
        return thermoList;
    }

    public void setThermoList(List<NestData> thermoList) {
        this.thermoList = thermoList;
    }

    public ThermoList(List<NestData> thermoList) {
        this.thermoList = thermoList;
    }

    private List<NestData> thermoList;


}
