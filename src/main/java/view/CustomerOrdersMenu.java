package view;

public class CustomerOrdersMenu extends Menu {
    public CustomerOrdersMenu(Menu parent){
        super("View Orders",parent);
        subMenus.put(1,getViewOrderMenu());
        subMenus.put(2,getRateProductMenu());
    }

    public Menu getViewOrderMenu(){
        return new Menu("View Order",this) {
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

    public Menu getRateProductMenu(){
        return new Menu("Rate Product",this) {
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
}