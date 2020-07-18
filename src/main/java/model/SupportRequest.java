package model;

import controller.Database;
import controller.RequestController;

import java.util.HashMap;

import static controller.Database.*;
import static controller.RequestController.allRequests;


public class SupportRequest extends Request {
    private HashMap<String, String> personInfo;

    public SupportRequest(HashMap<String, String> personInfo) {
        super(null);
        this.personInfo = personInfo;
        Database.saveToFile(this, createPath("support_requests", this.getRequestId()));
        allRequests.add(this);
    }

    @Override
    public void doThis() {
        RequestController.getInstance().addSupport (personInfo);
    }

    @Override
    public void decline() {
    }

    @Override
    public String show() {
        return personInfo.get("username") + " want to be a poor tech support";
    }
}