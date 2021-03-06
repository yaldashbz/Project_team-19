package fxmlController;

import static clientController.ServerConnection.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Cart;
import model.Product;
import model.Salesperson;
import view.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static view.App.getFXMLLoader;

public class CartMenuFXML implements Initializable { //todo

    private String backPageName;
    private GridPane ordersBox;
    @FXML private Label totalPrice;
    @FXML private Label discountCodeAmount;
    @FXML private GridPane basePane;
    @FXML private FontAwesomeIcon back;
    @FXML private AnchorPane anchorPane;

    public CartMenuFXML(String backPageName) {
        this.backPageName = backPageName;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ScrollPane scrollPane = new ScrollPane();
        ordersBox = new GridPane();
        ordersBox.setAlignment(Pos.CENTER);
        setCartsOnPane();
        ordersBox.setPadding(new Insets(10));
        ordersBox.setVgap(5);
        ordersBox.setHgap(5);
        scrollPane.setContent(ordersBox);
        basePane.add(scrollPane, 0 , 1);
        totalPrice.setText(String.valueOf(getCart().calculateTotalPrice()));
        System.out.println(backPageName);
        back.setOnMouseClicked ( event -> App.setRoot ( backPageName ) );

        back.setOnMousePressed ( event -> back.setStyle ( "-fx-font-family: FontAwesome; -fx-font-size: 20;-fx-effect: innershadow(gaussian, #17b5ff,75,0,5,0);" ) );

        back.setOnMouseReleased ( event -> back.setStyle ( "-fx-font-family: FontAwesome; -fx-font-size: 1em" ) );
        Cart cart = getCart();
        discountCodeAmount.setText(String.valueOf(cart.calculateTotalPrice () - cart.getTotalPriceAfterDiscountCode ()));
    }

    @FXML
    void discountCode(ActionEvent event) {
        Stage stage = new Stage();
        Parent fxml = null;
        try {
            fxml = getFXMLLoader ("discountCodeHandler").load ();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert fxml != null;
        stage.setScene(new Scene(fxml,300,200));
        stage.show();
    }

    @FXML
    void purchase(ActionEvent event) {
        if (token.length()==0) {
            App.showAlert(Alert.AlertType.ERROR,App.currentStage,"Error","You need to login.");
        } else if(!getPersonTypeByToken().equalsIgnoreCase("customer")) {
            App.showAlert(Alert.AlertType.ERROR,App.currentStage,"Error","You need to login with customer account.");
        }else {
            App.setRoot("pay");
        }
    }

    private void setCartsOnPane() {
        Cart cart=null;
        if (token.length()==0)
           cart = getCart();
        else
            cart = getCartAfterLogin();
        int rowIndex = 0;
        for (String productId : cart.getProductsForClient().keySet()) {
            for (String sellerName : cart.getProductsForClient().get(productId).keySet()){
                ProductInCart productInCart = new ProductInCart(cart.getProductsForClient().get(productId).get(sellerName));
                FXMLLoader loader = new FXMLLoader(CartMenuFXML.class.getResource("/fxml/productInCart.fxml"));
                loader.setController(productInCart);
                Parent parent = null;
                try {
                    parent = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent finalParent = parent;
                finalParent.setOnMouseClicked(e -> {
                    if (!productInCart.isProductInCart()){
                        ordersBox.getChildren().remove(finalParent);
                    }
                });
                ordersBox.add(parent, rowIndex % 2, rowIndex / 2);
                rowIndex++;
            }
        }
    }
}
