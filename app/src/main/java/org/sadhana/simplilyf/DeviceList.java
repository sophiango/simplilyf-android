package org.sadhana.simplilyf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sadhana on 11/20/15.
 */
public class DeviceList{


    public List<User.Light> getLights() {
        return lights;
    }

    public void setLights(List<User.Light> lights) {
        this.lights = lights;
    }

    public List<User.Thermo> getThermos() {
        return thermos;
    }

    public void setThermos(List<User.Thermo> thermos) {
        this.thermos = thermos;
    }

    private List<User.Light> lights = new ArrayList<User.Light>();

    private List<User.Thermo> thermos = new ArrayList<User.Thermo>();

    private String email;

    private String userId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public DeviceList(String email,String userId,String username,List<User.Light> lights,List<User.Thermo> thermos){
        this.lights=lights;
        this.thermos=thermos;
        this.email=email;
        this.userId=userId;
        this.username=username;
    }


}
