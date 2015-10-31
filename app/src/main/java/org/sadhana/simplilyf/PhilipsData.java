package org.sadhana.simplilyf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sadhana on 10/31/15.
 */


public class PhilipsData{

    private _1 _1;
    private _2 _2;
    private _3 _3;

    public PhilipsData._1 get_1() {
        return _1;
    }

    public void set_1(PhilipsData._1 _1) {
        this._1 = _1;
    }

    public PhilipsData._2 get_2() {
        return _2;
    }

    public void set_2(PhilipsData._2 _2) {
        this._2 = _2;
    }

    public PhilipsData._3 get_3() {
        return _3;
    }

    public void set_3(PhilipsData._3 _3) {
        this._3 = _3;
    }

    class _1{
        public JSONContent getJsonContent() {
            return jsonContent;
        }

        public void setJsonContent(JSONContent jsonContent) {
            this.jsonContent = jsonContent;
        }

        JSONContent jsonContent;
    }
    class _2{
        public JSONContent getJsonContent() {
            return jsonContent;
        }

        public void setJsonContent(JSONContent jsonContent) {
            this.jsonContent = jsonContent;
        }

        JSONContent jsonContent;
    }
    class _3{
        public JSONContent getJsonContent() {
            return jsonContent;
        }

        public void setJsonContent(JSONContent jsonContent) {
            this.jsonContent = jsonContent;
        }

        JSONContent jsonContent;
    }
}

class JSONContent {


    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public Pointsymbol getPointsymbol() {
        return pointsymbol;
    }

    public void setPointsymbol(Pointsymbol pointsymbol) {
        this.pointsymbol = pointsymbol;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonProperty("state")
    private State state;
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("modelid")
    private String modelid;
    @JsonProperty("swversion")
    private String swversion;
    @JsonProperty("pointsymbol")
    private Pointsymbol pointsymbol;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    class State {
        public Boolean getOn() {
            return on;
        }

        public void setOn(Boolean on) {
            this.on = on;
        }

        public Integer getBri() {
            return bri;
        }

        public void setBri(Integer bri) {
            this.bri = bri;
        }

        public Integer getHue() {
            return hue;
        }

        public void setHue(Integer hue) {
            this.hue = hue;
        }

        public Integer getSat() {
            return sat;
        }

        public void setSat(Integer sat) {
            this.sat = sat;
        }

        public List<Integer> getXy() {
            return xy;
        }

        public void setXy(List<Integer> xy) {
            this.xy = xy;
        }

        public Integer getCt() {
            return ct;
        }

        public void setCt(Integer ct) {
            this.ct = ct;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getEffect() {
            return effect;
        }

        public void setEffect(String effect) {
            this.effect = effect;
        }

        public String getColormode() {
            return colormode;
        }

        public void setColormode(String colormode) {
            this.colormode = colormode;
        }

        public Boolean getReachable() {
            return reachable;
        }

        public void setReachable(Boolean reachable) {
            this.reachable = reachable;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public void setAdditionalProperties(Map<String, Object> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }

        @JsonProperty("on")
        private Boolean on;
        @JsonProperty("bri")
        private Integer bri;
        @JsonProperty("hue")
        private Integer hue;
        @JsonProperty("sat")
        private Integer sat;
        @JsonProperty("xy")
        private List<Integer> xy = new ArrayList<Integer>();
        @JsonProperty("ct")
        private Integer ct;
        @JsonProperty("alert")
        private String alert;
        @JsonProperty("effect")
        private String effect;
        @JsonProperty("colormode")
        private String colormode;
        @JsonProperty("reachable")
        private Boolean reachable;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }

    class Pointsymbol{
        public String get_1() {
            return _1;
        }

        public void set_1(String _1) {
            this._1 = _1;
        }

        public String get_2() {
            return _2;
        }

        public void set_2(String _2) {
            this._2 = _2;
        }

        public String get_3() {
            return _3;
        }

        public void set_3(String _3) {
            this._3 = _3;
        }

        public String get_4() {
            return _4;
        }

        public void set_4(String _4) {
            this._4 = _4;
        }

        public String get_5() {
            return _5;
        }

        public void set_5(String _5) {
            this._5 = _5;
        }

        public String get_6() {
            return _6;
        }

        public void set_6(String _6) {
            this._6 = _6;
        }

        public String get_7() {
            return _7;
        }

        public void set_7(String _7) {
            this._7 = _7;
        }

        public String get_8() {
            return _8;
        }

        public void set_8(String _8) {
            this._8 = _8;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public void setAdditionalProperties(Map<String, Object> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }

        @JsonProperty("1")
        private String _1;
        @JsonProperty("2")
        private String _2;
        @JsonProperty("3")
        private String _3;
        @JsonProperty("4")
        private String _4;
        @JsonProperty("5")
        private String _5;
        @JsonProperty("6")
        private String _6;
        @JsonProperty("7")
        private String _7;
        @JsonProperty("8")
        private String _8;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }
}


