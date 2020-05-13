package view;

import controller.CartController;
import controller.PersonController;
import controller.ProductController;
import model.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewProductMenu extends Menu {
    Product product;
    public ViewProductMenu(Menu parent,Product product){
        super("View Product",parent);
        this.product = product;
        subMenus.put(1,getDigestMenu());
        subMenus.put(2,getAddToCartMenu());
        subMenus.put(3,getCommentMenu());
        subMenus.put(4,getCompareMenu());
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void show() {
        showProductDigest();
        super.show();
    }

    public void showProductDigest(){
        String firstTableFormat = "|%-36s|%-38s|";
        String sellersTableFormat = "|%-30s|%-30s|%-30s|";
        System.out.println(String.format("%s", "----------------------------------------------------------------------------"));
        System.out.println(String.format(firstTableFormat,"product ID","product Name"));
        System.out.println(String.format("%s", "----------------------------------------------------------------------------"));
        System.out.println(String.format(firstTableFormat,product.getID(),product.getName()));
        System.out.println(String.format("%s", "----------------------------------------------------------------------------"));
        System.out.println(String.format(firstTableFormat,"product average score",product.getAverageScore()));
        System.out.println(String.format("%s", "----------------------------------------------------------------------------"));
        System.out.println(String.format(firstTableFormat,product.getID(),product.getName()));
        System.out.println(String.format("%s", "----------------------------------------------------------------------------"));
        System.out.println(String.format(firstTableFormat,"property","value"));
        for (String s : product.getProperties().keySet()) {
            System.out.println(String.format("%s", "----------------------------------------------------------------------------"));
            System.out.println(String.format(firstTableFormat,s ,product.getProperties().get(s) ));
        }
        System.out.println(String.format("%s", "----------------------------------------------------------------------------"));
        System.out.println();
        System.out.println("all salesperson");
        for (OwnedProduct ownedProduct : ProductController.getProductsOfProduct(product)) {
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------"));
            if(ownedProduct.getSalesperson().isInDiscount(product))
            {
                System.out.println(String.format("%s %20s %s %20s %s %20s %5s","|",ownedProduct.getSellerName() , "|",ownedProduct.getPrice(),"|",ownedProduct.getSalesperson().getDiscountPrice(product),"|"));

            }else
                System.out.println(String.format("%s %20s %5s %20s %s %20s %5s","|",ownedProduct.getSellerName() , "|",ownedProduct.getPrice(),"|"," ","|"));
        }
        System.out.println(String.format("%s", "------------------------------------------------------------------------------------------"));

    }

    private Menu getDigestMenu(){
        return new Menu("Product Digest",this){
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                super.execute();
            }
        };
    }

    private Menu getAddToCartMenu () {
        return new Menu ( "Add to cart" , this ) {
            @Override
            public void show () {
                System.out.println ( this.getName () + " :" );
            }

            @Override
            public void execute () {
                boolean check = false;
                String input;
                do {
                    System.out.println("Enter wanted seller's username or press E to return to previous menu :");
                    input = scanner.nextLine();
                    if(input.equalsIgnoreCase("e")){
                        return;
                    }
                    if (getValidSeller(input)!=null){
                        CartController cartController = CartController.getInstance();
                        cartController.addProduct(product,getValidSeller(input));
                        check = true;
                    }
                    else {
                        System.out.println("This product does not have such seller");
                    }
                }while (!check);

            }
        };
    }

    public Menu getCompareMenu(){
        return new Menu("compare",this) {
            @Override
            public void show() {
                System.out.println("please enter another products's:");
            }

            @Override
            public void execute() {
                String id = getValidProductId();
                Product product2 = ProductController.searchProduct(id);
                ProductMenu.compareTwoProducts(product,product2);
                System.out.println("enter \"back\" to return.");
            }
        };
    }

    public Menu getCommentMenu(){
        return new Menu("Comments",this) {
            @Override
            public void show() {
                for (Comment comment : product.getComments()) {
                    if (comment.isCommentVerified()){
                        showComment(comment);
                    }
                }
                System.out.println();
                System.out.println("1. Add comment");
                System.out.println("2. Back");
            }

            @Override
            public void execute() {
                String num = getValidMenuNumber(2);
                if(num.equals("1")){
                    buildComment((Customer)PersonController.getLoggedInPerson());
                }else {
                    return;
                }
            }
        };
    }

    public Salesperson getValidSeller(String username){
        Salesperson salesperson = (Salesperson) PersonController.findPersonByUsername(username);
        if(ProductController.doesSellerHasProduct(product,salesperson))
            return salesperson;
        else return null;
    }


    public void showComment(Comment comment){
        System.out.println("commenter: "+comment.getCommenter().getUsername());
        System.out.println(comment.getDateTime());
        if(comment.isBought())
        {
            System.out.println("This user has bought this product");
        }else {
            System.out.println("This user has not bought this product");
        }
        System.out.println("Comment title: "+comment.getTitle()+"\n");
        System.out.println("Content: "+comment.getCommentString());
    }

    public void buildComment( Customer customer){
        System.out.println("please enter your comment title or enter \"..\" to return to previous menu: ");
        String commentTitle = scanner.nextLine();
        System.out.println("please enter your comment or enter \"..\" to return to previous menu: ");
        String string = scanner.nextLine();
        if(string.equalsIgnoreCase("..")){
            return;
        }else {
            Comment newComment = new Comment(true,customer,string,commentTitle);
            if(customer.isProductBought(product)){
                newComment.setBought(true);
            }
            product.addComment(newComment);
        }

    }
}