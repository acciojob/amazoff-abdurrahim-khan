package com.driver;

public class Order {

    private String id;
    private int deliveryTime;
    public Order()
    {

    }

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        int index = deliveryTime.indexOf(':');
        String hour = deliveryTime.substring(0,index);
        String minute = deliveryTime.substring(index+1);
        this.deliveryTime = (Integer.parseInt(hour)*60) + Integer.parseInt(minute);
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}

    public void setId(String id) {
        this.id = id;
    }

    public void setDeliveryTime(String deliveryTime) {

        int index = deliveryTime.indexOf(':');
        String hour = deliveryTime.substring(0,index);
        String minute = deliveryTime.substring(index+1);
        this.deliveryTime = (Integer.parseInt(hour)*60) + Integer.parseInt(minute);


    }
}
