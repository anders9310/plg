package plg.test;

import org.deckfour.xes.model.XLog;
import plg.generator.ProgressAdapter;
import plg.generator.log.LogGenerator;
import plg.generator.log.SimulationConfiguration;
import plg.generator.process.ProcessGenerator;
import plg.generator.process.RandomizationConfiguration;
import plg.io.exporter.PNMLExporter;
import plg.model.Process;

public class TestProcessGeneration {

    public static void main(String[] args) throws Exception {
        // process  randomization
        Process p =new Process("test" );
        ProcessGenerator.randomizeProcess(p, RandomizationConfiguration.BASIC_VALUES);

        // log simulation to generate 1000  traces
        XLog l =new LogGenerator(p, new SimulationConfiguration(), new ProgressAdapter()).generateLog();

        // export as  pnml
        new PNMLExporter().exportModel(p, "p.pnml" );

    }
}
