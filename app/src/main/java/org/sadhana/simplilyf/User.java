package org.sadhana.simplilyf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiango on 11/20/15.
 */
public class User implements Serializable{

    @JsonProperty("_id")
    private String Id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("username")
    private String username;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("__v")
    private Integer V;
    @JsonProperty("createAt")
    private String createAt;
    @JsonProperty("devicesAcc")
    private List<DevicesAcc> devicesAcc = new ArrayList<DevicesAcc>();
    @JsonProperty("lights")
    private List<Light> lights = new ArrayList<Light>();
    @JsonProperty("thermos")
    private List<Thermo> thermos = new ArrayList<Thermo>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getId() {
        return Id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getV() {
        return V;
    }

    public String getCreateAt() {
        return createAt;
    }

    public List<DevicesAcc> getDevicesAcc() {
        return devicesAcc;
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Thermo> getThermos() {
        return thermos;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public class Thermo implements Serializable{

        @JsonProperty("thermo_name")
        private String thermoName;
        @JsonProperty("thermo_id")
        private String thermoId;
        @JsonProperty("vendor")
        private String vendor;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getThermoName() {
            return thermoName;
        }

        public String getThermoId() {
            return thermoId;
        }

        public String getVendor() {
            return vendor;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }
    }

    public class Light implements Serializable{

        @JsonProperty("light_name")
        private String lightName;
        @JsonProperty("light_id")
        private String lightId;
        @JsonProperty("vendor")
        private String vendor;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getLightName() {
            return lightName;
        }

        public String getLightId() {
            return lightId;
        }

        public String getVendor() {
            return vendor;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }
    }

    public class DevicesAcc {

        @JsonProperty("password")
        private String password;
        @JsonProperty("username")
        private String username;
        @JsonProperty("vendor")
        private String vendor;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }

        public String getVendor() {
            return vendor;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }
    }
}
