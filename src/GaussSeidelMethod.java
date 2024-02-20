import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class GaussSeidelMethod {

    private final int n;
    private double[][] matrix;
    private final double[] bi;
    private double[] xik;
    private double[] xik_next;
    private final double[] di;
    private final double[][] cij;
    private final double eps;
    private final int m;
    private double[] error_vector;
    private boolean outputMode;

    public void setOutputMode(boolean outputMode){
        this.outputMode = outputMode;
    }

    public GaussSeidelMethod(int n, double[][] matrix, double[] bi, double eps, int m){
        this.n = n;
        this.matrix = matrix;
        this.eps = eps;
        this.bi = bi;
        this.m = m;
        this.xik = new double[n];
        this.xik_next = new double[n];
        this.error_vector = new double[n];
        this.di = new double[n];
        this.cij = new double[n][n];
    }

    public void solve(){
        System.out.println("-----------------------------------------");
        System.out.println("Матрица получена:");
        for (int i = 0; i < n; i++){
            System.out.println(Arrays.toString(matrix[i]) + " * x = " + bi[i] );
        }
        if(Det.determinant(matrix) == 0) {
            System.out.println("Данная матрица имеет детерминант, равный нулю, " +
                    "следовательно СЛАУ или не имеет решений, или имеет бесконечно много решений");
            System.exit(0);
        }
        System.out.println("-----------------------------------------");
        System.out.print("Проверяем диагональное преобладание: ");
        if(checkDiagonalDominance()) System.out.println("выполнено");
        else {
            System.out.println("невыполнено");
            System.out.println("Переставляем строки, чтобы достичь диагональное преобладание");
            rearrangeLines();
            System.out.println("Диагональное преобладание достигнуто, приступаем к решению СЛАУ");
            for (int i = 0; i < n; i++){
                System.out.println(Arrays.toString(matrix[i]) + " * x = " + bi[i] );
            }
        }
        System.out.println("-----------------------------------------");


        for(int i = 0; i < n; i++){
            di[i] = bi[i] / matrix[i][i];
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i == j) cij[i][j] = 0;
                else cij[i][j] = - matrix[i][j] / matrix[i][i];
            }
        }
        //первое приближение
        xik = di;
//        for(int i = 0; i < n; i++){
//            xik[i] = 1000;
//        }

        if(outputMode) {
            System.out.print("Первое приближение:");
            System.out.println(Arrays.toString(xik));
            System.out.println("-----------------------------------------");
        }

        for(int i = 0; i < m; i++){
            if(outputMode) {
                System.out.println("Итерация № " + (i + 1));
            }
            iteration();
            if(endConditional()) {
                System.out.println("СЛАУ решена!");
                System.out.println("Решение: " + Arrays.toString(xik_next));
                System.out.println("Вектор погрешностей: " + Arrays.toString(error_vector));
                System.out.println("Количество итераций: " + (i + 1));
                System.exit(0);
            } else {
                if(outputMode) {
                    System.out.println("Новое приближение:");
                    System.out.println(Arrays.toString(xik_next));
                    System.out.println("-----------------------------------------");
                }
                xik = xik_next;
                xik_next = new double[n];
            }

        }

        System.out.println("За данное количество итераций решение СЛАУ не было найдено, вожможно уравнение расходится и решений вообще нет");

    }

    private void iteration(){
        for(int i = 0; i < n; i++){
            double sum1 = 0, sum2 = 0;
            xik_next[i] = di[i];
            for(int j = 0; j < i ; j++){
                xik_next[i] += cij[i][j] * xik_next[j];
            }
            for(int j = i + 1; j < n; j++){
                xik_next[i] += cij[i][j] * xik[j];
            }
//            xik_next[i] = rounding(xik_next[i]);
        }
    }

    private double rounding(double number){
        BigDecimal help = new BigDecimal(number);
        help = help.setScale(4, RoundingMode.HALF_UP);
        return help.doubleValue();
    }

    private boolean checkDiagonalDominance(){
        for(int i = 0; i < n; i++){
            double sum = 0;
            for (int j = 0; j < n; j++){
                sum += Math.abs(matrix[i][j]);
            }
            sum -= Math.abs(matrix[i][i]);
            if(Math.abs(matrix[i][i]) < sum) return false;
        }
        return true;
    }

    private boolean checkLineDiagonalDominance(double[] line, int i){
        double sum = 0;
        for(int j = 0; j < n; j++){
            sum += Math.abs(line[j]);
        }
        sum -= Math.abs(line[i]);
        return Math.abs(line[i]) >= sum;
    }

    private void rearrangeLines(){
        boolean lineDominance = true;
        for(int i = 0; i < n && lineDominance; i++){
            if(!checkLineDiagonalDominance(matrix[i], i)){
                lineDominance = false;
                for(int j = i + 1; j < n; j++){
                    double[] help;
                    double bhelp;
                    if(checkLineDiagonalDominance(matrix[j], i)){
                        help = matrix[i];
                        matrix[i] = matrix[j];
                        matrix[j] = help;
                        bhelp = bi[i];
                        bi[i] = bi[j];
                        bi[j] = bhelp;
                        lineDominance = true;
                    }
                }
            }
        }
        if(!lineDominance) {
            System.out.println("Невозможно добиться диагонального преобладания в данной матрице");
            System.exit(0);
        }
    }

    //вернет true, если условие окончания выполняется и это была последняя итерация
    private boolean endConditional(){
        double error;
        boolean result = true;
        for(int i = 0; i < n; i++){
//            error = rounding(Math.abs(xik_next[i] - xik[i]));
            error = Math.abs(xik_next[i] - xik[i]);
            error_vector[i] = error;
            if(error > eps) result = false;
        }
        if(outputMode) {
            System.out.println("Максимальная погрешность: " + Arrays.stream(error_vector).max().getAsDouble());
        }
        return result;
    }
}
