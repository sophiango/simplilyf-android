package org.sadhana.simplilyf;

/**
 * Created by Sadhana on 10/31/15.
 */
public class PhilipsData {

    private Object state;
    private String type;
    private String name;



    private String modelId;
    private String swversion;
    private Object pointSymbol;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
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

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public Object getPointSymbol() {
        return pointSymbol;
    }

    public void setPointSymbol(Object pointSymbol) {
        this.pointSymbol = pointSymbol;
    }

}
