package org.sadhana.simplilyf;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sophiango on 11/13/15.
 */
public class LightList implements Serializable {

    public LightList(){

    }

    public List<QuickLightData> getlightList() {
        return lightList;
    }

    public void setlightList(List<QuickLightData> lightList) {
        this.lightList = lightList;
    }

    public LightList(List<QuickLightData> lightList) {
        this.lightList = lightList;
    }

    private List<QuickLightData> lightList;

    public List<QuickLightData> getLightList() {
        return lightList;
    }

    public void setLightList(List<QuickLightData> lightList) {
        this.lightList = lightList;
    }
    
    public static class QuickLightData implements Serializable{
        private String name;
        private String on;
        private String hue;

        public QuickLightData(){

        };

        public QuickLightData(String name, String on, String hue) {
            this.name = name;
            this.on = on;
            this.hue = hue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOn() {
            return on;
        }

        public void setOn(String on) {
            this.on = on;
        }

        public String getHue() {
            return hue;
        }

        public void setHue(String hue) {
            this.hue = hue;
        }
    }
}
