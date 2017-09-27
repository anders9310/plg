package plg.test.unit.process;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import plg.generator.process.*;

import static org.junit.Assert.fail;

public class ParameterRandomizationConfigurationTest {
    @Test
    public void testZeroObligation(){
        int numActivities = 10;
        int numGateways = 0;
        ParameterRandomizationConfiguration randomConfig = new ParameterRandomizationConfiguration(numActivities, numGateways);
        try{
            randomConfig.generateRandomPattern(true, true);
        }catch(Exception e){
            fail("Test threw an error: " + e.toString());
        }
    }
}
