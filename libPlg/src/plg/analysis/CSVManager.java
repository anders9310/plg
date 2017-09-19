package plg.analysis;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import plg.analysis.bpmeter.model.AnalysisResult;
import plg.analysis.bpmeter.model.MetricType;
import plg.utils.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVManager {

    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final Object [] FILE_HEADER = {"Model Name",MetricType.NUM_ACTIVITIES.getName(),MetricType.NUM_GATEWAYS.getName()};

    public void writeCsv(String filePath, String fileName, List<AnalysisResult> analysisResults){

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try{
            fileWriter = new FileWriter(filePath + "\\" + fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FILE_HEADER);

            for(AnalysisResult aRes : analysisResults){
                if(aRes!=null){
                    String modelName = aRes.getName();
                    int numberOfActivities = (int) aRes.findMetric(MetricType.NUM_ACTIVITIES.getName()).getValues().get(0).getValue();
                    int numberOfGateways = (int) aRes.findMetric(MetricType.NUM_GATEWAYS.getName()).getValues().get(0).getValue();

                    List<String> analysisResultList = new ArrayList<>();
                    analysisResultList.add(modelName);
                    analysisResultList.add(String.valueOf(numberOfActivities));
                    analysisResultList.add(String.valueOf(numberOfGateways));
                    csvFilePrinter.printRecord(analysisResultList);
                } else{
                    Logger.instance().info("Found null-result in list of model analysis results. Skipping writing result.");
                }

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
}
