package controller;

import model.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import static controller.Database.*;
import static model.Product.getProductById;

public class RequestController {
    public static ArrayList<Request> allRequests = new ArrayList<>();
    private static RequestController single_instance = null;

    private RequestController() {
    }

    public static RequestController getInstance() {
        if (single_instance == null)
            single_instance = new RequestController();

        return single_instance;
    }

    public enum FilterType {
        ALL, SALESPERSON, PRODUCT, DISCOUNT
    }

    public void initializeRequests() {
        for (File file : Database.returnListOfFiles(Database.address.get("discount_requests"))) {
            allRequests.add((DiscountRequest) read(DiscountRequest.class, file.getAbsolutePath()));
        }
        for (File file : Database.returnListOfFiles(address.get("product_requests"))) {
            allRequests.add((ProductRequest) read(ProductRequest.class, file.getAbsolutePath()));
        }
        for (File file : Database.returnListOfFiles(address.get("salesperson_requests"))) {
            allRequests.add((SalespersonRequest) read(SalespersonRequest.class, file.getAbsolutePath()));
        }
    }

    public void acceptRequest(Request request) {
        request.doThis();
        allRequests.remove(request);
        try {
            if (request instanceof DiscountRequest)
                deleteFile(createPath("discount_requests", request.getRequestId()));
            else if (request instanceof ProductRequest)
                deleteFile(createPath("product_requests", request.getRequestId()));
            else if (request instanceof SalespersonRequest)
                deleteFile(createPath("salesperson_requests", request.getRequestId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void declineRequest(Request request) {
        allRequests.remove(request);
        try {
            if (request instanceof DiscountRequest)
                deleteFile(createPath("discount_requests", request.getRequestId()));
            else if (request instanceof ProductRequest)
                deleteFile(createPath("product_requests", request.getRequestId()));
            else if (request instanceof SalespersonRequest)
                deleteFile(createPath("salesperson_requests", request.getRequestId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSalesPerson(HashMap<String, String> personInfo) {
        Salesperson salesperson = new Salesperson(personInfo);
        PersonController.getInstance().addPerson(salesperson);
    }

    public ArrayList<Request> filterByState(Request.RequestState requestState) {
        return allRequests.stream().filter(request -> request.getRequestState().
                equals(requestState)).collect(Collectors.toCollection(ArrayList::new));
    }

    public <T> ArrayList<Request> getSpecificTypeOfRequests(Class<T> requestType) {
        ArrayList<Request> requests = allRequests;
        return requests.stream().filter(requestType::isInstance).collect(Collectors.toCollection(ArrayList::new));
    }

    public void deleteProductRequest(String productId, Salesperson salesperson) {
        new ProductRequest(salesperson, getProductById(productId));
    }

    public void addProductRequest(Double price, Integer amount, Salesperson salesperson
            , String category, String name, String brand, HashMap<String, String> properties) {

        Product product = new Product(name, brand, category, properties, true);
        new ProductRequest(price, amount, salesperson, product);
    }

    public void editProductRequest(String price, String amount, Salesperson salesperson, String productID
            , String category, String name, String brand, HashMap<String, String> properties) {

        Product product = ProductController.getInstance().searchProduct(productID);
        double pr = (price == null) ? salesperson.getProductPrice(product) : Double.parseDouble(price);
        int am = (amount == null) ? salesperson.getProductAmount(product) : Integer.parseInt(amount);

        new ProductRequest(pr, am, salesperson, category, name, brand, properties, product);
    }

    public void deleteDiscountRequest(Discount discount, Salesperson salesperson) {
        new DiscountRequest(discount, salesperson);
    }

    public void addDiscountRequest(ArrayList<Product> add, LocalDateTime startTime, LocalDateTime endTime, Double discountPercentage, Salesperson salesperson) {

        new DiscountRequest(add, startTime, endTime, discountPercentage, salesperson);
    }

    public void editDiscountRequest(Discount discount, ArrayList<Product> add, ArrayList<Product> remove, LocalDateTime startTime,
                                    LocalDateTime endTime, Double discountPercentage, Salesperson salesperson) {
        ArrayList<Product> products = discount.getProducts();

        if (startTime == null)
            startTime = discount.getStartTime();
        if (endTime == null)
            endTime = discount.getEndTime();
        if (discountPercentage == null)
            discountPercentage = discount.getDiscountPercentage();

        products.addAll(add);
        products.removeAll(remove);

        //product haye nahayio bayad bedim besh
        new DiscountRequest(discount, products, startTime, endTime, discountPercentage, salesperson);
    }


    public Request getRequestById(String requestId) {
        for (Request request : allRequests) {
            if (request.getRequestId().equals(requestId))
                return request;
        }
        return null;
    }

}
