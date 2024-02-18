import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScannerManager scannerManager = new ScannerManager(scanner);

        if(scannerManager.sayInputMode()){
            scannerManager.setFileMode(false);
        } else {
            scannerManager.setScanner(scannerManager.sayNewScanner());
            scannerManager.setFileMode(true);
        }
        GaussSeidelMethod gaussSeidelMethod = scannerManager.saySLAE();
        gaussSeidelMethod.setOutputMode(scannerManager.sayOutputMode());
        gaussSeidelMethod.solve();
    }
}

//C:\Users\Elena\IdeaProjects\compMath\lab1\src\matrix\matrix1
