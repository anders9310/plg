package plg.analysis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import plg.analysis.model.bpmeter.AnalysisResult;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class BPMNProcessAnalyzer {
    BPMeterWrapper bpMeter = new BPMeterWrapper();
    Gson gson = new Gson();

    public AnalysisResult analyzeFile(File bpmnModel){
        String analysisResultJson = bpMeter.analyzeFile(bpmnModel);

        AnalysisResult result = gson.fromJson(analysisResultJson, AnalysisResult.class);

        //Type listType = new TypeToken<LinkedList>() {}.getType();
        //List<AnalysisResult> analysisResults = gson.fromJson(analysisResultJson, listType);
        //AnalysisResult result = analysisResults.get(0);

        return result;
    }
}
