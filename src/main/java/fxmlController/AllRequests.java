package fxmlController;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.Request;
import view.App;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static clientController.ServerConnection.*;

public class AllRequests implements Initializable {

    @FXML private VBox vBox;
    @FXML private FontAwesomeIcon back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Metadata.allRequests = this;
        updateTable();
    }

    public void updateTable () {
        vBox.getChildren ().clear ();
        ArrayList<Request> allRequests = new ArrayList<>();
        allRequests.addAll(getSalespersonRequests());
        allRequests.addAll(getProductRequests());
        allRequests.addAll(getFileRequests());
        allRequests.addAll(getDiscountRequests());
        allRequests.addAll(getAuctionRequests());
        for (Request request : allRequests) {
            RequestCard card = new RequestCard(request);
            FXMLLoader loader = new FXMLLoader(AllRequests.class.getResource("/fxml/requestCard.fxml"));
            loader.setController(card);
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            vBox.getChildren().add(parent);
        }
    }

    @FXML private void backSizeBig ( MouseEvent mouseEvent ) {
        back.setStyle ( "-fx-font-family: FontAwesome; -fx-font-size: 20;-fx-effect: innershadow(gaussian, #17b5ff,75,0,5,0);" );
    }

    @FXML private void backSizeSmall ( MouseEvent mouseEvent ) {
        back.setStyle ( "-fx-font-family: FontAwesome; -fx-font-size: 1em" );
    }

    @FXML private void back (MouseEvent mouseEvent) {
        App.setRoot ( "managerMenu" );
    }
}
