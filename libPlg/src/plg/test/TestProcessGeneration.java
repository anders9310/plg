package plg.test;

import plg.generator.process.ParameterRandomizationConfiguration;
import plg.generator.process.ProcessGenerator;
import plg.model.Process;

public class TestProcessGeneration {

    public static void main(String[] args) throws Exception {
        // process  randomization
        Process p =new Process("test" );
        ProcessGenerator.randomizeProcess(p, ParameterRandomizationConfiguration.BASIC_VALUES);
        System.out.print(p.toString());
    }
}
