import com.google.gson.reflect.TypeToken;
import controller.Database;
import model.Category;
import model.Product;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class GsonTest {
//    public void GsonTest {
//        ArrayList <Test> tests = new ArrayList<Test>();
//        tests.add(new Test("yalda", 1));
//        tests.add(new Test("solale",2));
//        tests.add(new Test("alireza",3));
//        Database.setAddress(System.getProperty("user.dir") + "text2.json");
//        Type collectionType = new TypeToken<Collection<Test>>() {}.getType();
//        Database.write(tests, collectionType);
//        System.out.println(Database.handleJsonArray("password"));
////        ArrayList<Test> allTests = new ArrayList<Test>();
////        allTests.add(new Test("hello", "1"));
////        allTests.add(new Test("salam", "2"));
////        Database.setAddress(System.getProperty("user.dir") + "text.json");
////
////        Type collectionType = new TypeToken<Collection<Test>>() {}.getType();
////
////        Database.write(allTests, collectionType);
////        System.out.println(allTests = Database.read(collectionType));
////        allTests.add(new Test("goodbye", "1"));
////        Database.write(allTests, new TypeToken<ArrayList<Test>>() {}.getType());
////        System.out.println(Database.read(collectionType));
//    }
//    class Test {
//        public String name;
//        public int password;
//
//        public Test(String name, int password) {
//            this.name = name;
//            this.password = password;
//        }
//    }

    @Test
    public void fileTest() {
        try {
            System.out.println(Database.deleteFile("C:\\Users\\HAMID\\Desktop\\json\\json.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testForGSONObjInObj () {
        Category category = new Category(true, "labaniat", null);

        Product product = new Product("1", "panir", "lighvan", "yeki",
                Product.ProductState.BUILD_IN_PROGRESS, category);

        //aval properties category ro comment kon bad test kon
        try {
            Database.write(product, product.getClass(), "C:\\Users\\HAMID\\Desktop\\json.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void deleteFile () {
        File temp = new File ("C:\\Users\\HAMID\\Desktop\\json\\json.json");
        try {
            if (temp.createNewFile()) {
                System.out.println("file created");
            } else {
                System.out.println("file exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}