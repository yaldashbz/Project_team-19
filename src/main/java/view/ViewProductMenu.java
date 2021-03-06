package view;

import controller.CartController;
import controller.PersonController;
import controller.ProductController;
import model.*;
import model.wagu.Block;
import model.wagu.Board;
import model.wagu.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ViewProductMenu extends Menu {
    Product product;

    public ViewProductMenu(Menu parent) {
        super("View Product", parent);
        subMenus.put(1, getDigestMenu());
        subMenus.put(2, getAddToCartMenu());
        subMenus.put(3, getCommentMenu());
        subMenus.put(4, getCompareMenu());
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            product.increaseSeen();
        }
    }

    @Override
    public void show() {
        fancyTitle();
        if (product != null)
            System.out.println("Product : " + product.getID());
        for (Integer menuNum : subMenus.keySet()) {
            System.out.println(menuNum + ". " + subMenus.get(menuNum).getName());
        }
        System.out.println((subMenus.size() + 1) + ". User Menu");
        if (this.parentMenu != null)
            System.out.println((subMenus.size() + 2) + ". Back");
        else
            System.out.println((subMenus.size() + 2) + ". Exit");
        System.out.println("?. Help");
    }

    @Override
    public void execute() {
        if (product == null) {
            System.out.println("Please enter product id:");
            String id = getValidProductId();
            if (id.equals(BACK_BUTTON))
                return;
            product = ProductController.getInstance().getProductById(id);
        }
        super.execute();
    }


    public static void showProductDigest(Product product) {
        String firstTableFormat = "|%-36s|%-38s|";
        String sellersTableFormat = "|%-30s|%-30s|%-30s|";
        System.out.println(String.format("%s", LINE));
        System.out.println(String.format(firstTableFormat, "product ID", "product Name"));
        System.out.println(String.format("%s", LINE));
        System.out.println(String.format(firstTableFormat, product.getID(), product.getName()));
        System.out.println(String.format("%s", LINE));
        System.out.println(String.format(firstTableFormat, "product average score", product.getAverageScore()));
        System.out.println(String.format("%s", LINE));
        System.out.println(String.format(firstTableFormat, product.getID(), product.getName()));
        System.out.println(String.format("%s", LINE));
        System.out.println(String.format(firstTableFormat, "property", "value"));
        for (String s : product.getProperties().keySet()) {
            System.out.println(String.format("%s", LINE));
            System.out.println(String.format(firstTableFormat, s, product.getProperties().get(s)));
        }
        System.out.println(String.format("%s", LINE));
        System.out.println();
        System.out.println("all salesperson");
        for (OwnedProduct ownedProduct : ProductController.getInstance().getProductsOfProduct(product)) {
            System.out.println(String.format("%s", LINE));
            if (ownedProduct.getSalesperson().isInDiscount(product)) {
                System.out.println(String.format(sellersTableFormat, "|", ownedProduct.getSellerName(), "|", ownedProduct.getPrice(), "|", ownedProduct.getSalesperson().getDiscountPrice(product), "|"));

            } else
                System.out.println(String.format(sellersTableFormat, "|", ownedProduct.getSellerName(), "|", ownedProduct.getPrice(), "|", "", "|"));
        }
        System.out.println(String.format("%s", LINE));

    }

    public static void showProduct(Product product) {
        List<String> headersList = Arrays.asList("Product ID", "Name", "Brand", "Average Score", "Least Price", "Category");
        List<List<String>> rowsList = new ArrayList<>();
        List<String> row = new ArrayList<>(6);
        row.add(product.getID());
        row.add(product.getName());
        row.add(product.getBrand());
        row.add(String.valueOf(product.getAverageScore()));
        row.add(String.valueOf(product.getLeastPrice()));
        row.add(product.getCategory().getName());
        rowsList.add(row);
        Board board = new Board(85);
        Table table = new Table(board, 85, headersList, rowsList);
        List<Integer> colAlignList = Arrays.asList(
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER);
        table.setColAlignsList(colAlignList);
        Block tableBlock = table.tableToBlocks();
        board.setInitialBlock(tableBlock);
        board.build();
        String tableString = board.getPreview();
        System.out.println(tableString);
    }

    public static void showSalesPersons(Product product) {
        List<String> headersList = Arrays.asList("Seller", "Price", "(In Discount)");
        List<List<String>> rowsList = new ArrayList<>();
        ArrayList<Salesperson> sellers =  ProductController.stock.get(product);
        sellers = sellers.stream().sorted(Comparator.comparingDouble(s -> s.getProductPrice(product))).collect(Collectors.toCollection(ArrayList::new));
        for (Salesperson seller : sellers) {
            List<String> row = new ArrayList<>(3);
            row.add(seller.getUsername());
            row.add(seller.getProductPrice(product)+ "$");
            row.add(String.valueOf(seller.isInDiscount(product) ? seller.getDiscountPrice(product) : "-"));
            rowsList.add(row);
        }
        Board board = new Board(75);
        Table table = new Table(board, 75, headersList, rowsList);
        List<Integer> colAlignList = Arrays.asList(
                Block.DATA_CENTER,
                Block.DATA_CENTER,
                Block.DATA_CENTER);
        table.setColAlignsList(colAlignList);
        Block tableBlock = table.tableToBlocks();
        board.setInitialBlock(tableBlock);
        board.build();
        String tableString = board.getPreview();
        System.out.println(tableString);
    }

    private Menu getDigestMenu() {
        return new Menu("Product Digest", this) {
            @Override
            public void show() {
                showProduct(product);
                showSalesPersons(product);
                super.show();
            }

            @Override
            public void execute() {
                super.execute();
            }
        };
    }

    private Menu getAddToCartMenu() {
        return new Menu("Add to cart", this) {
            @Override
            public void show() {
                fancyTitle();
            }

            @Override
            public void execute() {
                boolean check = false;
                String input;
                System.out.println("Enter wanted seller's username or press \"..\" to return to previous menu :");
                do {
                    input = scanner.nextLine();
                    if (!PersonController.getInstance().isTherePersonByUsername(input)) {
                        System.out.println("there is no person with such username.");
                    }
                    if (input.equals(BACK_BUTTON)) {
                        return;
                    }
                    if (getValidSeller(input) != null) {
                        if (!ProductController.getInstance().isProductAvailable(product, getValidSeller(input))) {
                            System.out.println("Sorry mojud nadarim.");
                            return;
                        }
                        //CartController.getInstance().addProduct(product, getValidSeller(input));
                        System.out.println("Successfully added.");
                        check = true;
                    } else {
                        System.out.println("This product does not have such seller.");
                    }
                } while (!check);

            }
        };
    }

    public Menu getCompareMenu() {
        return new Menu("compare", this) {
            @Override
            public void show() {
                System.out.println("Please enter another products:");
            }

            @Override
            public void execute() {
                String id = getValidProductId();
                Product product2 = ProductController.getInstance().getProductById(id);
                ProductMenu.compareTwoProducts(product, product2);
                System.out.println(BACK_HELP);
            }
        };
    }

    public Menu getCommentMenu() {
        return new Menu("Comments", this) {
            @Override
            public void show() {

                for (Comment comment : product.getComments()) {
                    if (comment.isCommentVerified()) {
                        showComment(comment);
                    }
                }
                System.out.println();
//                if (PersonController.getInstance().isLoggedInPersonCustomer()) {
//                    System.out.println("Enter \"..\" to return.");
//                    System.out.println("1. Add comment");
//                } else {
//                    System.out.println("1. Back.");
//                }
            }

            @Override
            public void execute() {
                String num = getValidMenuNumber(1, 1);
//                if (PersonController.getInstance().isLoggedInPersonCustomer()) {
//                    buildComment((Customer) PersonController.getInstance().getLoggedInPerson());
//                }
            }
        };
    }

    public Salesperson getValidSeller(String username) {
        if (PersonController.getInstance().isTherePersonByUsername(username) && PersonController.getInstance().getPersonByUsername(username) instanceof Salesperson) {
            Salesperson salesperson = (Salesperson) PersonController.getInstance().getPersonByUsername(username);
            if (ProductController.getInstance().doesSellerHasProduct(product, salesperson))
                return salesperson;
        }
        return null;
    }


    public void showComment(Comment comment) {
        System.out.println("commenter: " + comment.getCommenter().getUsername());
        System.out.println(comment.getDateTime());
        if (comment.isBought()) {
            System.out.println("This user has bought this product");
        } else {
            System.out.println("This user has not bought this product");
        }
        System.out.println("Comment title: " + comment.getTitle() + "\n");
        System.out.println("Content: " + comment.getCommentString());
    }

    public void buildComment(Customer customer) {
        System.out.println("please enter your comment title or enter \"..\" to return to previous menu: ");
        String commentTitle = scanner.nextLine();
        System.out.println("please enter your comment or enter \"..\" to return to previous menu: ");
        String string = scanner.nextLine();
        if (!string.equals(BACK_BUTTON)) {
            Comment newComment = new Comment(true, customer, string, commentTitle);
            if (customer.isProductBought(product)) {
                newComment.setBought(true);
            }
            product.addComment(newComment);
        }

    }


}
