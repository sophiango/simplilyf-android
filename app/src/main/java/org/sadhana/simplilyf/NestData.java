package org.sadhana.simplilyf;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by SophiaNgo on 9/11/15.
 */
public class NestData implements Serializable{

    String name;
    Double target_temperature;
    Double target_temperature_high;
    Double target_temperature_low;
    String target_temperature_mode;
    String mode;

    public NestData(String thermo_name, Double target_temperature, Double target_temperature_high, Double target_temperature_low, String target_temperature_mode, String thermo_mode) {
        this.name = thermo_name;
        this.target_temperature = target_temperature;
        this.target_temperature_high = target_temperature_high;
        this.target_temperature_low = target_temperature_low;
        this.target_temperature_mode = target_temperature_mode;
        this.mode = thermo_mode;
    }

    public String getName() {
        return name;
    }

    public void setThermo_name(String thermo_name) {
        this.name = name;
    }

    public Double getTarget_temperature() {
        return target_temperature;
    }

    public void setTarget_temperature(Double target_temperature) {
        this.target_temperature = target_temperature;
    }

    public Double getTarget_temperature_high() {
        return target_temperature_high;
    }

    public void setTarget_temperature_high(Double target_temperature_high) {
        this.target_temperature_high = target_temperature_high;
    }

    public Double getTarget_temperature_low() {
        return target_temperature_low;
    }

    public void setTarget_temperature_low(Double target_temperature_low) {
        this.target_temperature_low = target_temperature_low;
    }

    public String getTarget_temperature_mode() {
        return target_temperature_mode;
    }

    public void setTarget_temperature_mode(String target_temperature_mode) {
        this.target_temperature_mode = target_temperature_mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}