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
        try
        {
            orderRepository.orderHashMap.put(order.getId(), order);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void addPartner(String partnerId)
    {
        try
        {
            DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
            orderRepository.partnerHashMap.put(partnerId,deliveryPartner);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }
    public void orderToPartner(String orderId, String partnerId)
    {
        try
        {
            if(!orderRepository.orderHashMap.containsKey(orderId) || !orderRepository.partnerHashMap.containsKey(partnerId))
            {
                return;
            }
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
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public Order getOrder(String orderId)
    {
        Order order = null;
        try
        {
            if(!orderRepository.orderHashMap.containsKey(orderId)) return null;
            order = orderRepository.orderHashMap.get(orderId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return order;

    }
    public DeliveryPartner getDeliveryPartner(String partnerId)
    {
        DeliveryPartner deliveryPartner = null;
        try
        {
            if(!orderRepository.partnerHashMap.containsKey(partnerId)) return null;
            deliveryPartner =  orderRepository.partnerHashMap.get(partnerId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return deliveryPartner;
    }
    public int noOfOrdersToPartner(String partnerId)
    {
        int num =0;
        try
        {
            if(!orderRepository.partnerHashMap.containsKey(partnerId)) return 0;
            num = orderRepository.partnerHashMap.get(partnerId).getNumberOfOrders();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return num;
    }
    public List<String> listOfOrdersToPartner(String partnerId)
    {
        List<String> list = new ArrayList<>();
        try
        {
            if(!orderRepository.partnerHashMap.containsKey(partnerId)) return new ArrayList<>();
            list = orderRepository.listOfOrdersHashMap.get(partnerId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    public List<String> listOfAllOrders()
    {

        List<String> listOfAllOrders = new ArrayList<>();
        try
        {
            if(orderRepository.orderHashMap.size() == 0) return new ArrayList<>();

            for(String orderId : orderRepository.orderHashMap.keySet())
            {
                listOfAllOrders.add(orderId);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return listOfAllOrders;

    }
    public int noOfUnassignedOrders()
    {
        int num =0;
        try
        {
            num = orderRepository.orderHashMap.size() - orderRepository.orderPartnerHashMap.size();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return num;
    }
    public int noOfUndeliveredOrders(String partnerId, String time)
    {
        int count =0;
        try
        {
            if(!orderRepository.partnerHashMap.containsKey(partnerId) || !orderRepository.listOfOrdersHashMap.containsKey(partnerId))
            {
                return 0;
            }

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

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return count;

    }
    public String getLastDeliveryTime(String partnerId)
    {
        String ans = "";
        try
        {
            if(!orderRepository.partnerHashMap.containsKey(partnerId) || !orderRepository.listOfOrdersHashMap.containsKey(partnerId))
            {
                return "";
            }
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

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ans;

    }
    public void deleteOrder(String orderId)
    {

        try
        {
            if(!orderRepository.orderHashMap.containsKey(orderId)) return;
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
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void deletePartner(String partnerId)
    {

        try
        {
            if(!orderRepository.partnerHashMap.containsKey(partnerId)) return;
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
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
