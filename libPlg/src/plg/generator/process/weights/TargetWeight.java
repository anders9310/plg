package plg.generator.process.weights;

import plg.generator.process.CurrentGenerationState;
import plg.generator.process.Metric;
import plg.generator.process.Target;
import plg.generator.process.RandomizationPattern;

import java.util.ArrayList;
import java.util.List;

public class TargetWeight extends Weight{
    RandomizationPattern randomizationPattern;
    Metric metric;
    List<Benefit> benefits;

    public TargetWeight(RandomizationPattern randomizationPattern, List<RandomizationPattern> allRandomizationPatterns, Target target){
        this.randomizationPattern = randomizationPattern;
        this.metric = target.getType();
        benefits = new ArrayList<>();
        for(RandomizationPattern pattern : allRandomizationPatterns){
            Benefit pow = new Benefit(pattern, target);
            benefits.add(pow);
        }
    }

    @Override
    protected double calculateValue(CurrentGenerationState state) {
        double rawWeightGivenToThisPattern = rawWeightGivenToThisPattern(state);
        double rawWeightGivenToAllPatterns = rawWeightGivenToAllPatternsAbs(state);
        //if(!isAnythingBeneficial(state)){
        //if(benefits.get(0).getProcess().getNumUnknownComponents()<=3){
            //get weights based on which contribute to reducing potential
            double rawWeightGivenToThisPatternPotential = rawWeightGivenThisPatternBasedOnPotential();
            double rawWeightGivenToAllPatternsPotential = rawWeightGivenToAllPatternsBasedOnPotentialAbs();

            //assure that the potential weights count for 50 % of the total weight, by superimposing it on the other weights
            //double rawWeightGivenToThisPatternPotentialNorm = rawWeightGivenToThisPatternPotential / rawWeightGivenToAllPatternsPotential;
            double equalizationFactor = rawWeightGivenToAllPatternsPotential!=0 ? rawWeightGivenToAllPatterns / rawWeightGivenToAllPatternsPotential : 0;
            double weightGivenToThisPatternPotential = rawWeightGivenToThisPatternPotential * equalizationFactor;
            double weightGivenToAllPatternsPotential = rawWeightGivenToAllPatternsPotential * equalizationFactor;
            rawWeightGivenToThisPattern += weightGivenToThisPatternPotential;
            rawWeightGivenToAllPatterns += weightGivenToAllPatternsPotential;
        //}
        return rawWeightGivenToThisPattern / rawWeightGivenToAllPatterns;
    }

    private double rawWeightGivenThisPatternBasedOnPotential() {
        return rawWeightGivenToPatternBasedOnPotential(randomizationPattern);
    }

    private double rawWeightGivenToPatternBasedOnPotential(RandomizationPattern pattern){
        Benefit thisProductionTargetWeight = findProductionObligatWeightForRandomizationPattern(pattern);
        return thisProductionTargetWeight.calculatePotentialGrowthBenefit();
    }

    private double rawWeightGivenToAllPatternsBasedOnPotentialAbs() {
        double sumOfWeights = 0;
        for(Benefit benefit : benefits){
            sumOfWeights += Math.abs(rawWeightGivenToPatternBasedOnPotential(benefit.getRandomizationPattern()));
        }
        return sumOfWeights;
    }

    private boolean isAnythingBeneficial(CurrentGenerationState state) {
        boolean isAnythingBeneficial = false;
        for(Benefit pow : benefits){
            if(pow.getValue(state) > 0){
                isAnythingBeneficial = true;
            }
        }
        return isAnythingBeneficial;
    }


    private double rawWeightGivenToAllPatternsAbs(CurrentGenerationState state){
        double sumOfWeights = 0;
        for(Benefit pow : benefits){
            sumOfWeights += Math.abs(pow.getValue(state));
        }
        return sumOfWeights;
    }

    private double rawWeightGivenToThisPattern(CurrentGenerationState state){
        Benefit thisProductionTargetWeight = findProductionObligatWeightForRandomizationPattern(this.randomizationPattern);
        if(thisProductionTargetWeight !=null){
            return thisProductionTargetWeight.calculateValue(state);
        }else{
            throw new RuntimeException("Unexpected TargetWeight State");
        }
    }

    private Benefit findProductionObligatWeightForRandomizationPattern(RandomizationPattern randomizationPattern){
        for(Benefit pow : benefits){
            if(pow.getRandomizationPattern()==randomizationPattern){
                return pow;
            }
        }
        return null;
    }
}
