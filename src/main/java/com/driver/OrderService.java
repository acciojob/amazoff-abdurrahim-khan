package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    public void addOrder(Order order)
    {
        orderRepository.orderHashMap.put(order.getId(), order);
    }
    public void addPartner(String partnerId)
    {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        orderRepository.partnerHashMap.put(partnerId,deliveryPartner);
    }
    public void orderToPartner(String orderId, String partnerId)
    {
        if(!orderRepository.orderPartnerHashMap.containsKey(orderId))
        {
            orderRepository.orderPartnerHashMap.put(orderId,partnerId);

            if(orderRepository.listOfOrdersHashMap.containsKey(partnerId))
            {
                List<String> orderList = orderRepository.listOfOrdersHashMap.get(partnerId);
                orderList.add(orderId);

            }
            else
            {
                List<String> orderList = new ArrayList<>();
                orderList.add(orderId);
                orderRepository.listOfOrdersHashMap.put(partnerId,orderList);
            }
            DeliveryPartner deliveryPartner = orderRepository.partnerHashMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
        }
    }
    public Order getOrder(String orderId)
    {
        return orderRepository.orderHashMap.get(orderId);
    }
    public DeliveryPartner getDeliveryPartner(String partnerId)
    {
        return orderRepository.partnerHashMap.get(partnerId);
    }
    public int noOfOrdersToPartner(String partnerId)
    {
        return orderRepository.partnerHashMap.get(partnerId).getNumberOfOrders();
    }
    public List<String> listOfOrdersToPartner(String partnerId)
    {
        return orderRepository.listOfOrdersHashMap.get(partnerId);
    }
    public List<String> listOfAllOrders()
    {
        List<String> listOfAllOrders = new ArrayList<>();
        for(String orderId : orderRepository.orderHashMap.keySet())
        {
            listOfAllOrders.add(orderId);
        }
        return listOfAllOrders;
    }
    public int noOfUnassignedOrders()
    {
        return orderRepository.orderHashMap.size() - orderRepository.orderPartnerHashMap.size();
    }
    public int noOfUndeliveredOrders(String partnerId, String time)
    {
        int count =0;
        int index = time.indexOf(':');
        String hour = time.substring(0,index);
        String minute = time.substring(index+1);
        int timelimit = (Integer.parseInt(hour)*60) + Integer.parseInt(minute);

        List<String> listOfOrders = orderRepository.listOfOrdersHashMap.get(partnerId);
        for(String orderId : listOfOrders)
        {
            Order order = orderRepository.orderHashMap.get(orderId);
            if(order.getDeliveryTime() > timelimit)
            {
                count++;
            }
        }
        return count;
    }
    public String getLastDeliveryTime(String partnerId)
    {
        int maxTime = 0;
        List<String> listOfOrders = orderRepository.listOfOrdersHashMap.get(partnerId);
        for(String orderId : listOfOrders)
        {
            int orderTime = orderRepository.orderHashMap.get(orderId).getDeliveryTime();
            if(orderTime > maxTime)
            {
                maxTime = orderTime;
            }
        }
        int hour = maxTime/60;
        int minute = maxTime%60;
        String ans = "";
        if(hour < 10)
        {
            ans = ans + "0";
        }
        ans = ans + hour+":";
        if(minute < 10)
        {
            ans = ans + "0";
        }
        ans = ans + minute;
        return ans;
    }
    public void deleteOrder(String orderId)
    {
        orderRepository.orderHashMap.remove(orderId);

        if(orderRepository.orderPartnerHashMap.containsKey(orderId))
        {
            String partnerId = orderRepository.orderPartnerHashMap.get(orderId);
            orderRepository.orderPartnerHashMap.remove(orderId);

            List<String> listOfOrdersToPartner = orderRepository.listOfOrdersHashMap.get(partnerId);
            int i =0;
            for(;i<listOfOrdersToPartner.size(); i++)
            {
                if(listOfOrdersToPartner.get(i).equals(orderId))
                {
                    break;
                }
            }
            listOfOrdersToPartner.remove(i);
            DeliveryPartner deliveryPartner = orderRepository.partnerHashMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()-1);
        }
    }
    public void deletePartner(String partnerId)
    {

        orderRepository.partnerHashMap.remove(partnerId);
        if(orderRepository.listOfOrdersHashMap.containsKey(partnerId))
        {
            List<String> listOfAssignedOrders = orderRepository.listOfOrdersHashMap.get(partnerId);
            for(String orderId : listOfAssignedOrders)
            {
                orderRepository.orderPartnerHashMap.remove(orderId);
            }
            orderRepository.listOfOrdersHashMap.remove(partnerId);
        }
    }
}
