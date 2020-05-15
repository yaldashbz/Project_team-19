package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    public static HashMap<String, String> address = new HashMap<>();

    public static void initializeAddress() {
        String databaseAddress = System.getProperty("user.dir") + File.separator + "database";
        address.put("customers", databaseAddress + File.separator + "persons" + File.separator + "customers");
        address.put("managers", databaseAddress + File.separator + "persons" + File.separator + "managers");
        address.put("salespersons", databaseAddress + File.separator + "persons" + File.separator + "salespersons");
        address.put("products", databaseAddress + File.separator + "products");
        address.put("discount_codes", databaseAddress + File.separator + "discount_codes");
        address.put("stock.json", databaseAddress);
        address.put("root_categories.json", databaseAddress);
        address.put("product_requests", databaseAddress + File.separator + "requests" + File.separator + "product_requests");
        address.put("discount_requests", databaseAddress + File.separator + "requests" + File.separator + "discount_requests");
        address.put("salesperson_requests", databaseAddress + File.separator + "requests" + File.separator + "salesperson_requests");
    }

    public static <T> Object read(Type typeOfT, String address) {
        try {
            GsonBuilder builder = new GsonBuilder().setLenient().enableComplexMapKeySerialization();
            Gson gson = builder.create();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(address));
            return gson.fromJson(bufferedReader, typeOfT);
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }

    public static <T> void write(T obj, String address) {
        try {
            GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
            Gson gson = builder.create();
            FileWriter writer = new FileWriter(address);
            writer.write(gson.toJson(obj));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static <T> void editInFile(T obj, String keyPath, String fileName) {
        try {
            String filePath = createPath(keyPath, fileName);
            deleteFile(filePath);

            String newFilePath = createPath(keyPath, obj.toString());
            write(obj, newFilePath);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static <T> void saveToFile(T object, String address) {
        Database.write(object, address);
    }

    public static String handleJsonObject(JsonReader reader, String wantedFieldName) {
        try {
            reader.beginObject();
            String fieldName = null;

            while (reader.hasNext()) {
                JsonToken token = reader.peek();

                if (token.equals(JsonToken.END_OBJECT)) {
                    reader.endObject();
                } else {
                    if (token.equals(JsonToken.NAME)) {
                        fieldName = reader.nextName();
                    }
                    if (wantedFieldName.equals(fieldName)) {
                        reader.peek();
                        return reader.nextString();
                    } else
                        reader.skipValue();
                }
            }
            return null;

        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    public static ArrayList<String> handleJsonArray(String filedName, String address) throws IOException {
        JsonReader reader = new JsonReader(new FileReader(address));
        ArrayList<String> arrayList = new ArrayList<>();
        reader.beginArray();
        while (true) {
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
                break;
            } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
                arrayList.add(handleJsonObject(reader, filedName));
            } else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        return arrayList;
    }

    public static String createPath(String keyPath, String name) {
        return address.get(keyPath) + File.separator + name + ".json";
    }

    public static File[] returnListOfFiles(String folderAddress) {
        File folder = new File(folderAddress);
        return folder.listFiles();
    }

    public static boolean deleteFile(String fileAddress) throws IOException {
        File file = new File(fileAddress);
        if (!file.createNewFile()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }
}
