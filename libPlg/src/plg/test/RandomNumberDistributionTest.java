package plg.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RandomNumberDistributionTest {

    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final Object [] FILE_HEADER = {"Number"};
    private static final String PROJECT_ROOT_FOLDER = System.getProperty("user.dir");
    private static final String PROJECT_TEST_FOLDER = PROJECT_ROOT_FOLDER + "\\src\\plg\\test";
    private static final String RESULT_FILE_FOLDER = PROJECT_TEST_FOLDER + "\\analysisresults";
    private static final String RESULT_FILE_PATH = RESULT_FILE_FOLDER + "\\randomnumbertestresults.csv";

    private final static int NUM = 100000;
    private final static int DESIRED_MEAN = 15;

    public static void main(String[] args){
        //printDifferences();
        generateRandomNumbersPoissonStyle();
    }

    private static void generateRandomNumbersPoissonStyle(){
        List<Number> results = new LinkedList<>();
        for(int i = 0; i<NUM; i++){
            results.add(getRandomNumberPoissonDistributionWithPotential());
        }
        printToCsv(results);
    }

    private static void printDifferences(){
        List<Number> differences = new LinkedList<>();
        double lambda = DESIRED_MEAN;
        double prevProb = 0;
        for(int k = 0; k<DESIRED_MEAN*2;k++){
            double sum = getSumExpression(lambda, k);
            double curProb = Math.exp(-lambda) * sum ;
            differences.add(curProb-prevProb);
            prevProb = curProb;
        }
        printToCsv(differences);
    }

    private static int getSizeInverseTransformSampling(){
        double lambda = DESIRED_MEAN;
        int k = 0;
        double p = Math.exp(-lambda);
        double s = p;
        double u = Math.random();
        while(u>s){
            k++;
            p *= lambda/k;
            s += p;
            u = Math.random();
        }
        return k;
    }

    private static int getRandomNumberPoissonDistributionWithPotential(){
        double lambda = DESIRED_MEAN;
        int k = 0;
        int p = 1;

        double probContinueForAllPrevious = 1.0 - Math.exp(-lambda);
        double probStopAtKTotal = Math.exp(-lambda)*lambdaPowIDividedByIFactorial(lambda, 1) / probContinueForAllPrevious;
        double probReducePotential = probStopAtKTotal;
        double u = Math.random();
        while(p>0){
            if(u>probReducePotential){
                p++;
                probContinueForAllPrevious*= 1.0-probStopAtKTotal;
                probStopAtKTotal = Math.exp(-lambda) * lambdaPowIDividedByIFactorial(lambda,k+p) / probContinueForAllPrevious;
                probReducePotential = Math.pow(probStopAtKTotal, 1.0 / (double)p);
            }else{
                k++;
                p--;
            }

            u = Math.random();
        }
        return k;
    }

    private static int getRandomNumberPoissonDistributionRandomNumberRecalculation(){
        double lambda = DESIRED_MEAN;
        int k = 0;
        double probSuccessAtK = Math.exp(-lambda);
        double probFailForAllPrevious = 1;

        double u = Math.random();
        while(u>probSuccessAtK){
            k++;
            probFailForAllPrevious*= 1.0-probSuccessAtK;
            probSuccessAtK = Math.exp(-lambda) * lambdaPowIDividedByIFactorial(lambda,k) / probFailForAllPrevious;
            u = Math.random();
        }
        return k;
    }

    private static void printToCsv(List<Number> results){
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try{
            fileWriter = new FileWriter(RESULT_FILE_PATH);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FILE_HEADER);

            for(Number number : results){
                List<String> analysisResultList = new ArrayList<>();
                analysisResultList.add(number.toString());
                csvFilePrinter.printRecord(analysisResultList);
            }
        }catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }

    private static double getSumExpression(double lambda, double k) {
        double sum = 0;
        for(int i = 0;i<=k; i++){
            sum += lambdaPowIDividedByIFactorial(lambda, i);
        }
        return sum;
    }

    private static double lambdaPowIDividedByIFactorial(double lambda, int i){
        double product = 1;
        for(int c = 1; c<=i; c++){
            product *= lambda/c;
        }
        return product;
    }
}
