package org.sadhana.simplilyf;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by SophiaNgo on 9/11/15.
 */
public class NestData {
    @JsonProperty("$version")
    private Integer $version;
    @JsonProperty("$timestamp")
    private long $timestamp;
    @JsonProperty("auto_away")
    private Integer autoAway;
    @JsonProperty("auto_away_learning")
    private String autoAwayLearning;
    @JsonProperty("can_cool")
    private Boolean canCool;
    @JsonProperty("can_heat")
    private Boolean canHeat;
    @JsonProperty("compressor_lockout_enabled")
    private Boolean compressorLockoutEnabled;
    @JsonProperty("compressor_lockout_timeout")
    private Integer compressorLockoutTimeout;
    @JsonProperty("current_temperature")
    private Integer currentTemperature;
    @JsonProperty("hvac_ac_state")
    private Boolean hvacAcState;
    @JsonProperty("hvac_alt_heat_state")
    private Boolean hvacAltHeatState;
    @JsonProperty("hvac_alt_heat_x2_state")
    private Boolean hvacAltHeatX2State;
    @JsonProperty("hvac_aux_heater_state")
    private Boolean hvacAuxHeaterState;
    @JsonProperty("hvac_cool_x2_state")
    private Boolean hvacCoolX2State;
    @JsonProperty("hvac_emer_heat_state")
    private Boolean hvacEmerHeatState;
    @JsonProperty("hvac_fan_state")
    private Boolean hvacFanState;
    @JsonProperty("hvac_heat_x2_state")
    private Boolean hvacHeatX2State;
    @JsonProperty("hvac_heat_x3_state")
    private Boolean hvacHeatX3State;
    @JsonProperty("hvac_heater_state")
    private Boolean hvacHeaterState;
    @JsonProperty("name")
    private String name;
    @JsonProperty("target_change_pending")
    private Boolean targetChangePending;
    @JsonProperty("target_temperature")
    private Double targetTemperature;
    @JsonProperty("target_temperature_high")
    private Integer targetTemperatureHigh;
    @JsonProperty("target_temperature_low")
    private Integer targetTemperatureLow;
    @JsonProperty("target_temperature_type")
    private String targetTemperatureType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer get$version() {
        return $version;
    }

    public long get$timestamp() {
        return $timestamp;
    }

    public Integer getAutoAway() {
        return autoAway;
    }

    public String getAutoAwayLearning() {
        return autoAwayLearning;
    }

    public Boolean getCanCool() {
        return canCool;
    }

    public Boolean getCanHeat() {
        return canHeat;
    }

    public Boolean getCompressorLockoutEnabled() {
        return compressorLockoutEnabled;
    }

    public Integer getCompressorLockoutTimeout() {
        return compressorLockoutTimeout;
    }

    public Integer getCurrentTemperature() {
        return currentTemperature;
    }

    public Boolean getHvacAcState() {
        return hvacAcState;
    }

    public Boolean getHvacAltHeatState() {
        return hvacAltHeatState;
    }

    public Boolean getHvacAltHeatX2State() {
        return hvacAltHeatX2State;
    }

    public Boolean getHvacAuxHeaterState() {
        return hvacAuxHeaterState;
    }

    public Boolean getHvacCoolX2State() {
        return hvacCoolX2State;
    }

    public Boolean getHvacEmerHeatState() {
        return hvacEmerHeatState;
    }

    public Boolean getHvacFanState() {
        return hvacFanState;
    }

    public Boolean getHvacHeatX2State() {
        return hvacHeatX2State;
    }

    public Boolean getHvacHeatX3State() {
        return hvacHeatX3State;
    }

    public Boolean getHvacHeaterState() {
        return hvacHeaterState;
    }

    public String getName() {
        return name;
    }

    public Boolean getTargetChangePending() {
        return targetChangePending;
    }

    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public Integer getTargetTemperatureHigh() {
        return targetTemperatureHigh;
    }

    public Integer getTargetTemperatureLow() {
        return targetTemperatureLow;
    }

    public String getTargetTemperatureType() {
        return targetTemperatureType;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
}