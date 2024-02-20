import exceptions.FileException;
import exceptions.IncorrectValueException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ScannerManager {
    private Scanner scanner;
    private boolean fileMode;
    public ScannerManager(Scanner scanner){
        this.scanner = scanner;
    }

    public void setScanner(Scanner scanner){
        this.scanner = scanner;
    }

    public void setFileMode(boolean fileMode){
        this.fileMode = fileMode;
    }
    //вернет true если ввод с клавиатуры
    public boolean sayInputMode(){
        boolean flag = false;
        while(!flag) {
            try {
                System.out.println("Вы хотите ввести данные с клавиатуры или из файла? (k/f)");
                String ans = scanner.nextLine().trim();
                switch (ans) {
                    case "" ->
                        throw new NullPointerException();
                    case "k" -> {
                        flag = true;
                        return true;
                    }
                    case "f" -> {
                        flag = true;
                        return false;
                    }
                    default -> throw new IncorrectValueException();
                }
            } catch (IncorrectValueException | NullPointerException e){
                System.out.println("Ответ должен быть \"k\" или \"f\"");
            }
        }
        return flag;
    }


    public boolean sayOutputMode(){
        boolean flag = false;
        while(!flag) {
            try {
                System.out.print("Нужно выводить результат каждой итерации решения? (y/n)");
                String ans = scanner.nextLine().trim();
                if(fileMode) System.out.println(ans);
                switch (ans) {
                    case "" ->
                        throw new NullPointerException();
                    case "y" -> {
                        flag = true;
                        return true;
                    }
                    case "n" -> {
                        flag = true;
                        return false;
                    }
                    default -> throw new IncorrectValueException();
                }
            } catch (IncorrectValueException | NullPointerException e){
                System.out.println("Ответ должен быть \"y\" или \"n\"");
                if(fileMode) errorEnd();
            }
        }
        return flag;
    }

    public GaussSeidelMethod saySLAE(){
        int n = sayN();
        return new GaussSeidelMethod(n, sayMatrix(n), sayBi(n),sayEpsilon(),  sayM());
    }

    public Scanner sayNewScanner(){
        String sFile;
        Scanner scanner1 = null;
        while(scanner1 == null){
            try{
                System.out.println("Введите путь к файлу с данными:");
                sFile = scanner.nextLine().trim();
                if(sFile.isEmpty()) throw new NullPointerException();
                File file = new File(sFile);
                if(file.exists() && !file.canRead()) throw new FileException();
                scanner1 = new Scanner(file);
            } catch (NullPointerException e){
                System.out.println("Путь не может быть пустым");
            } catch (FileException e){
                System.out.println("Данные из файла невозможно прочитать");
            } catch (FileNotFoundException e){
                System.out.println("Файл не найден");
            }
        }
        return scanner1;
    }

    public double sayEpsilon(){
        double num = 0;
        String sNum;
        while (num < 0.000001 || num > 1){
            try {
                System.out.print("Введите значение точности [0.000001; 1]: ");
                sNum = scanner.nextLine().trim();
                if(fileMode) System.out.println(sNum);
                if(sNum.isEmpty()) throw new NullPointerException();
                num = Double.parseDouble(sNum);
                if(num < 0.000001 || num > 1) throw new IncorrectValueException();
            } catch (IncorrectValueException e){
                System.out.println("Значение точности должно быть положительным числом из промежутка [0.000001; 1]");
                if(fileMode) errorEnd();
            } catch (NullPointerException e){
                System.out.println("Значение точности не может быть пустым");
                if(fileMode) errorEnd();
            }   catch (NumberFormatException e){
                System.out.println("Количество итераций должно быть числом");
                if(fileMode) errorEnd();
            }
        }
        return num;
    }

    public int sayM(){
        int num = 0;
        String sNum;
        while (num <= 0){
            try {
                System.out.print("Введите максимальное количество итераций: ");
                sNum = scanner.nextLine().trim();
                if(fileMode) System.out.println(sNum);
                if(sNum.isEmpty()) throw new NullPointerException();
                num = Integer.parseInt(sNum);
                if(num <= 0) throw new IncorrectValueException();
            } catch (IncorrectValueException e){
                System.out.println("Значение количества итераций должно быть положительным");
                if(fileMode) errorEnd();
            } catch (NullPointerException e){
                System.out.println("Количество итераций не может быть пустым");
                if(fileMode) errorEnd();
            }  catch (NumberFormatException e){
                System.out.println("Количество итераций должно быть целым числом");
                if(fileMode) errorEnd();
            }
        }
        return num;
    }

    public int sayN() {
        int num = 0;
        String sNum;
        while (num <= 0){
            try {
                System.out.print("Введите размерность матрицы: ");
                sNum = scanner.nextLine().trim();
                if(fileMode) System.out.println(sNum);
                if(sNum.isEmpty()) throw new NullPointerException();
                num = Integer.parseInt(sNum);
                if(num <= 0 || num > 20) throw new IncorrectValueException();
            } catch (IncorrectValueException e){
                System.out.println("Значение размерности должно быть положительным и не больше 20");
                if(fileMode) errorEnd();
            } catch (NullPointerException e){
                System.out.println("Значение размерности не может быть пустым");
                if(fileMode) errorEnd();
            } catch (NumberFormatException e){
                System.out.println("Значение размерности должно быть целым числом");
                if(fileMode) errorEnd();
            }
        }
        return num;
    }


    private double sayDoubleNumber(){
        Double num = null;
        String sNum;
        while (num == null){
            try {
                sNum = scanner.nextLine().trim();
                if(fileMode) System.out.println(sNum);
                if(sNum.isEmpty()) throw new NullPointerException();
                num = Double.valueOf(sNum);
            } catch (NullPointerException e){
                System.out.println("Коэфициент не может быть пустым");
                if(fileMode) errorEnd();
            } catch (NumberFormatException e){
                num = null;
                System.out.println("Коэфициент должен быть числом");
                if(fileMode) errorEnd();
            }
        }
        return num;
    }


    public double[] sayBi(int n){
        System.out.println("Введите правые части уравнений по одному:");
        double[] bi = new double[n];
        for (int i = 0; i < n; i++){
            bi[i] = sayDoubleNumber();
        }
        return bi;
    }

    public double[][] sayMatrix(int n){
        System.out.println("Введите коэфициенты матрицы по строкам через пробел:");
        double[][] matrix = new double[n][n];
        String[] line;
        boolean flag;
        for (int i = 0; i < n; i++){
            flag = false;
            while (!flag) {
                try {
                    line = (scanner.nextLine().trim() + " ").split(" ", n);
                    if(fileMode) System.out.println(Arrays.toString(line));
                    for (int j = 0; j < n; j++) {
                        matrix[i][j] = Double.parseDouble(line[j]);
                        flag = true;
                    }
                } catch (NumberFormatException e){
                    System.out.println("Коэфициенты матрицы должны быть числами и их должно быть " + n);
                    flag = false;
                    if(fileMode) errorEnd();
                }
            }
        }
        return matrix;
    }

    private void errorEnd(){
        System.out.println("В файле неверные данные, программа завершена");
        System.exit(0);
    }

}
