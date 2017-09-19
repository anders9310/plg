package plg.analysis;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import plg.analysis.bpmeter.BPMeterWrapper;
import plg.analysis.bpmeter.model.AnalysisResult;
import plg.utils.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class BPMNProcessAnalyzer {
    CSVManager csvManager;
    BPMeterWrapper bpMeter = new BPMeterWrapper();
    Gson gson = new Gson();

    public AnalysisResult analyzeModel(File bpmnModelFile){
        List<File> files = new LinkedList<>();
        files.add(bpmnModelFile);

        List<AnalysisResult> analysisResult = analyzeModels(files);
        return analysisResult.get(0);
    }

    public List<AnalysisResult> analyzeModels(List<File> bpmnModelFiles){
        Logger.instance().info("Starting process analysis for list of files");

        String analysisResultJson = bpMeter.analyzeFiles(bpmnModelFiles);

        List<AnalysisResult> result;
        try{
            result = gson.fromJson(analysisResultJson, new TypeToken<List<AnalysisResult>>(){}.getType());
        } catch(JsonParseException e){
            Logger.instance().error("Error while deserializing JSON: " + "\n" +
                    "Error message" + e.toString());
            result = null;
        }

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
