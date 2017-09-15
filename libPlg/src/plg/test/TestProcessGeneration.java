package plg.test;

import plg.generator.process.*;
import plg.model.Process;

public class TestProcessGeneration {

    public static void main(String[] args) throws Exception {
        int maxValue = 1000000;
        // process  randomization
        Process p =new Process("test" );
        ObligationsProcessGenerator.randomizeProcess(p, ParameterRandomizationConfiguration.BASIC_VALUES);
        //Plg2ProcessGenerator.randomizeProcess(p, new Plg2RandomizationConfiguration(maxValue,maxValue,0.1,0.2,0.1,0.7,0.3,0.3,maxValue,0.1));
        System.out.print(p.toString());
    }
}
