import controller.Database;
import controller.ProductController;
import model.Category;
import model.Product;
import org.junit.Test;
import view.ProductMenu;
import view.ViewProductMenu;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class productMenuTest {
    private Object ProductState;

    @Test
    public void viewProductTest() throws IOException {
        Database.initializeAddress();
        Category category = new Category("labaniat", null, new HashSet<>());
        HashSet<String> fields = new HashSet<>();
        fields.add("color");
        fields.add("size");
        category.setPropertyFields(fields);
        HashMap<String, String> properties1 = new HashMap<>();
        properties1.put("color", "yellow");
        properties1.put("size", "big");
        HashMap<String ,String> properties2 = new HashMap<>();
        properties2.put("color", "white");
        properties2.put("size", "small");

        Product product1 = new Product( "panir", "lighvan", category.getName(), properties1, false);
        ProductController.allProducts.add(product1);
        ProductMenu productMenu = new ProductMenu(null);
        ViewProductMenu viewProductMenu = new ViewProductMenu(null);
    }

}
