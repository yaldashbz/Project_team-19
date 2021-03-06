package fxmlController;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Discount;
import model.DiscountRequest;
import model.Product;
import model.Salesperson;
import view.App;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class DiscountRequestHandler implements Initializable {

    Discount discount;

    enum requestMode {
        EDIT, ADD
    }

    public DiscountRequestHandler() {
        mode = requestMode.ADD;
    }

    public DiscountRequestHandler(Discount discount) {
        this.discount = discount;
        mode = requestMode.EDIT;
    }

    private requestMode mode;

    @FXML
    private TextArea products;

    @FXML
    private TextField percentage;

    @FXML
    private DatePicker startTime;

    @FXML
    private DatePicker endTime;

    @FXML
    private Button send;

    @FXML
    private FontAwesomeIcon back;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (mode.equals(requestMode.EDIT)) {
            percentage.setText(String.valueOf(discount.getDiscountPercentage()));
        }
        send.setOnAction(e->send());
        back.setOnMouseClicked ( event -> {
            App.setRoot ( "allDiscounts" );
        } );
        back.setOnMousePressed ( event -> {
            back.setStyle ( "-fx-font-family: FontAwesome; -fx-font-size: 20;-fx-effect: innershadow(gaussian, #17b5ff,75,0,5,0);" );
        } );
        back.setOnMouseReleased ( event -> {
            back.setStyle ( "-fx-font-family: FontAwesome; -fx-font-size: 1em" );
        } );

    }

    void send() {
        System.out.println("todo");
//        Salesperson salesperson = (Salesperson) PersonController.getInstance().getLoggedInPerson();
//        String[] ids = products.getText().split("-");
//        ArrayList<String> productArrayList = new ArrayList<>(Arrays.asList(ids));
//        ArrayList<Product> validProducts= new ArrayList<>();
//        if (mode.equals(requestMode.EDIT)) {
//        DiscountRequest discountRequest = new DiscountRequest(discount,productArrayList,startTime.getValue().atStartOfDay(),endTime.getValue().atStartOfDay(),Double.parseDouble(percentage.getText()),salesperson);
//        } else {
//            if(percentage.getText().length()==0){
//                App.error("Fill Blank Fields!");
//            }else {
//                for (String s : productArrayList) {
//                    if (!ProductController.getInstance().isThereProductById(s)||!ProductController.getInstance().doesSellerHasProduct(ProductController.getInstance().getProductById(s),salesperson)){
//                        App.error("Product Ids are not valid!");
//                        return;
//                    }else {
//                        validProducts.add(ProductController.getInstance().getProductById(s));
//                    }
//
//                }
//            }
//            RequestController.getInstance().addDiscountRequest(validProducts, startTime.getValue().atStartOfDay(), endTime.getValue().atStartOfDay(),Double.parseDouble( percentage.getText()), salesperson);
//        }
    }
}
