package plg.analysis;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import plg.analysis.bpmeter.model.RawAnalysisResult;
import plg.analysis.bpmeter.model.MetricType;
import plg.utils.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CSVManager {

    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final Object [] FILE_HEADER = {"Model Name",MetricType.NUM_ACTIVITIES.getName(),MetricType.NUM_GATEWAYS.getName(), MetricType.NUM_AND_GATES.getName(), MetricType.NUM_XOR_GATES.getName()};

    public void writeCsv(String filePath, String fileName, List<RawAnalysisResult> rawAnalysisResult){

        List<String> headerNames = new LinkedList<>();
        headerNames.add("ModelName");
        for(MetricType m : MetricType.values()){
            headerNames.add(m.getName());
        }
        Object [] fileHeader = headerNames.toArray();

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try{
            fileWriter = new FileWriter(filePath + "\\" + fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(fileHeader);

            for(RawAnalysisResult aRes : rawAnalysisResult){
                if(aRes!=null){
                    List<String> analysisResultList = new ArrayList<>();

                    String modelName = aRes.getName();
                    analysisResultList.add(modelName);

                    for(MetricType m : MetricType.values()){
                        double metricValue = aRes.findMetric(m.getName()).getValues().get(0).getValue();
                        analysisResultList.add(String.valueOf(metricValue));
                    }

                    /*int numberOfActivities = (int) aRes.findMetric(MetricType.NUM_ACTIVITIES.getName()).getValues().get(0).getValue();
                    int numberOfGateways = (int) aRes.findMetric(MetricType.NUM_GATEWAYS.getName()).getValues().get(0).getValue();
                    int numberOfAnd = (int) aRes.findMetric(MetricType.NUM_AND_GATES.getName()).getValues().get(0).getValue();
                    int numberOfXor = (int) aRes.findMetric(MetricType.NUM_XOR_GATES.getName()).getValues().get(0).getValue();
                    analysisResultList.add(String.valueOf(numberOfActivities));
                    analysisResultList.add(String.valueOf(numberOfGateways));
                    analysisResultList.add(String.valueOf(numberOfAnd));
                    analysisResultList.add(String.valueOf(numberOfXor));*/

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
