package plg.analysis;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import plg.analysis.bpmeter.BPMeterDataModelHandler;
import plg.analysis.bpmeter.BPMeterWrapper;
import plg.analysis.bpmeter.model.AnalysisResult;
import plg.utils.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class BPMNProcessAnalyzer {
    CSVManager csvManager;
    BPMeterWrapper bpMeter = new BPMeterWrapper();

    public AnalysisResult analyzeModel(File bpmnModelFile){
        List<File> files = new LinkedList<>();
        files.add(bpmnModelFile);

        List<AnalysisResult> analysisResult = analyzeModels(files);
        return analysisResult.get(0);
    }

    public List<AnalysisResult> analyzeModels(List<File> bpmnModelFiles){
        Logger.instance().info("Starting process analysis for list of files");

        String analysisResultJson = bpMeter.analyzeFiles(bpmnModelFiles);
        List<AnalysisResult> result = BPMeterDataModelHandler.convertJsonResultToDataModel(analysisResultJson);

        Logger.instance().info("Process analysis complete for list of files");
        return result;
    }

    public void exportAnalysisResultsToCsv(String filePath, String fileName, List<AnalysisResult> analysisResults){
        Logger.instance().info("Starting process analysis result exportation: " + fileName);
        csvManager = new CSVManager();
        csvManager.writeCsv(filePath, fileName, analysisResults);
        Logger.instance().info("Process analysis result exportation finished: " + fileName);
    }
}
