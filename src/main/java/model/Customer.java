package model;

import controller.Database;
import controller.PersonController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends Person {
    private ArrayList<BuyLog> buyLogs;
    private HashMap<DiscountCode,Integer> discountCodes;
    private double credit;
    private Cart cart;


    public Customer(HashMap<String, String> personInfo) throws IOException {
        super (personInfo);
        discountCodes = new HashMap<DiscountCode, Integer>();
        buyLogs = new ArrayList<BuyLog>();
        Database.saveToFile(this,Database.createPath("customer",personInfo.get("username")));
    }

    public DiscountCode findDiscountCodeByCode(String code){
        for (DiscountCode discountCode : discountCodes.keySet()) {
            if(discountCode.getCode().equals(code))
                return discountCode;
        }
        return null;
    }

    public boolean isThereDiscountCodeByCode(String code){
        return findDiscountCodeByCode(code)!=null;
    }

    public Cart getCart() {
        return cart;
    }


    public void useDiscountCode(DiscountCode discountCode){
        discountCodes.put(discountCode,discountCodes.get(discountCode)-1);
        if(discountCodes.get(discountCode)==0){
            discountCodes.remove(discountCode);
        }
    }

    public boolean checkCredit () {
        // Comparing cart price with credit
        return false;
    }

    public void setCredit ( double credit ) {
        this.credit = credit;
    }

    public double getCredit () {
        return credit;
    }

    public void getLog () {

    }

    public void updateHistory () {

    }


}

