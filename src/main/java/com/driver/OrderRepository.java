package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,DeliveryPartner> partnerHashMap;
    HashMap<String,Order> orderHashMap;
    HashMap<String, List<String>> listOfOrdersHashMap;
    HashMap<String,String> orderPartnerHashMap;

    public OrderRepository()
    {
        partnerHashMap = new HashMap<>();
        orderHashMap = new HashMap<>();
        listOfOrdersHashMap = new HashMap<>();
        orderPartnerHashMap = new HashMap<>();
    }



}
