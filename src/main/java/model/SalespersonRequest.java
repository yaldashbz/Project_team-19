package model;

import controller.RequestController;

import java.io.IOException;
import java.util.HashMap;


public class SalespersonRequest extends Request {
    private HashMap<String, String> personInfo;

    public SalespersonRequest(HashMap<String, String> personInfo, String requestId) throws IOException {
        super(requestId, null);
        this.personInfo = personInfo;
    }

    @Override
    public void doThis() {
        RequestController.addSalesPerson(personInfo);
    }

    @Override
    public String show() {
        //ba tavajoh be saligh etaghireah bedim
        return personInfo.get("username") + " for salesperson" + "\n" + "request id = " + getRequestId();
    }

    public String toString() {
        return "seller" + personInfo;
    }
}
