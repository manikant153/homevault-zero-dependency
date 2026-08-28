package homevault;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" HomeVault");
        System.out.println(" Offline Property Data and Price Estimator");
        System.out.println(" Zero external dependencies");
        System.out.println("========================================");

        PropertyRepository propertyRepository = new PropertyRepository();
        Shell shell = new Shell(propertyRepository);
        shell.start();
    }
}