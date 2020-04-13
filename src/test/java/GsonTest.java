import controller.GsonEx;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.Arrays;

public class GsonTest {
    @Test
    public void getFieldsGson() throws IOException {
        String[] array = new String[]{"20", "30", "18", "28"};
        Assert.assertEquals(Arrays.asList(array), GsonEx.handleJsonArray("C:\\Users\\solale\\IdeaProjects\\OnlineShoppingProject\\src\\main\\resources\\gson.txt","age"));
    }

}
