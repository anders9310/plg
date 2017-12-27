package plg.generator.process.weights;

import plg.generator.process.*;
import plg.model.Process;

public class Benefit extends Weight {
    private final double POTENTIAL_THRESHOLD = 1;
    private final double VALUE_GRANULARITY = 0.01;

    private Target target;
    private RandomizationPattern randomizationPattern;
    private Process process;

    public Benefit(RandomizationPattern randomizationPattern, Target target) {
        this.randomizationPattern = randomizationPattern;
        this.target = target;
        this.process = target.getProcess();
    }

    protected double calculateValue(CurrentGenerationState state) {
        if (target.getMean() == 0) {
            return 0;
        }

        double metricContribution = process.getContributionOf(state, this.target.getType(), randomizationPattern);
        double targetValue = target.getTargetValue();
        double currentValue = target.getCurrentValue();
        //double currentPotential = process.getNumPlaceholderComponents();
        //double potentialIncrease = process.getPlaceholderIncrease(randomizationPattern);

        double currentStateOfBenefit = (currentValue - targetValue) / targetValue;
        double projectedStateOfBenefit = (currentValue + metricContribution - targetValue) / targetValue;
        return compare(Math.abs(currentStateOfBenefit), Math.abs(projectedStateOfBenefit), 0);
    }

    public double calculatePotentialGrowthBenefit() {
        double potentialGrowthContribution = process.getPotentialIncreaseOf(randomizationPattern);
        final double TARGET_RELATIVE_GRANULARITY = 0.05;

        double currentValue = target.getCurrentValue();
        double targetValue = target.getTargetValue();
        double currentDistance = (currentValue - targetValue) / targetValue;
        //double diff = compare(targetValue, currentValue, VALUE_GRANULARITY);

        boolean shouldDecreasePlaceholders = process.getNumPlaceholderComponents() > 5 || ((!isLikelyARatio(targetValue) && currentDistance > -TARGET_RELATIVE_GRANULARITY) || (isLikelyARatio(targetValue) && Math.abs(currentDistance) < TARGET_RELATIVE_GRANULARITY));
        boolean shouldIncreasePlaceholders = process.getNumPlaceholderComponents() <= 2 && ((!isLikelyARatio(targetValue) && currentDistance < -TARGET_RELATIVE_GRANULARITY) || (isLikelyARatio(targetValue) && Math.abs(currentDistance) > TARGET_RELATIVE_GRANULARITY));
        if (shouldDecreasePlaceholders) {//If should decrease
            if (potentialGrowthContribution > 0) {//If grows
                return -1;
            } else {
                return 1;
            }
        } else if (shouldIncreasePlaceholders) {//If should grow
            if (potentialGrowthContribution > 0) {//If grows
                return 1;
            } else {
                return -1;
            }
        } else {//We don't care
            return 0;
        }
    }

    public RandomizationPattern getRandomizationPattern() {
        return randomizationPattern;
    }

    public Process getProcess() {
        return process;
    }

    public Target getTarget() {
        return target;
    }

    private static boolean isLikelyARatio(double targetValueOfMetric) {
        return hasDecimalDigits(targetValueOfMetric);
    }

    private static boolean hasDecimalDigits(double number) {
        double decimals = number - Math.floor(number);
        return decimals > 0.0;
    }

    public static double compare(double value, double comparedTo, double threshold) {
        if (value > comparedTo + threshold) {
            return value - comparedTo;
        } else if (value < comparedTo - threshold) {
            return value - comparedTo;
        } else {
            return 0;
        }
    }
}
