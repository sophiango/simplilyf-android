package org.sadhana.simplilyf;

/**
 * Created by sophiango on 11/21/15.
 */
 public class Config {
    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }

    public String getPHILIPS_IP_ADDRESS() {
        return PHILIPS_IP_ADDRESS;
    }

    /* Comment out the one you are not using */

//    final String PHILIPS_IP_ADDRESS = "http://10.189.238.94:3000";
//    final String IP_ADDRESS = "http://52.8.12.57:3000"; // Cloud server
  //  final String IP_ADDRESS = "http://10.189.238.94:3000";
    final String PHILIPS_IP_ADDRESS = "http://10.189.18.218:3000"; // change to IP address of the machine running Philips Hue Emulator
    final String IP_ADDRESS = "http://52.8.12.57:3000"; // Cloud server
//    final String IP_ADDRESS = "http://10.189.18.218:3000";

//    final String IP_ADDRESS = "http://172.16.1.9:3000";
//    final String IP_ADDRESS = "http://192.168.1.9:3000";

}
