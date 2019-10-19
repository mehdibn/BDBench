package tn.lipsic.babel.generator;

import tn.lipsic.babel.Utils;

/**
 * A generator of an exponential distribution. It produces a sequence
 * of time intervals according to an exponential
 * distribution.  Smaller intervals are more frequent than larger
 * ones, and there is no bound on the length of an interval.  When you
 * construct an instance of this class, you specify a parameter gamma,
 * which corresponds to the rate at which events occur.
 * Alternatively, 1/gamma is the average length of an interval.
 */
public class ExponentialGenerator extends NumberGenerator {
    // What percentage of the readings should be within the most recent exponential.frac portion of the dataset?
    public static final String EXPONENTIAL_PERCENTILE_PROPERTY = "exponential.percentile";
    public static final String EXPONENTIAL_PERCENTILE_DEFAULT = "95";

    // What fraction of the dataset should be accessed exponential.percentile of the time?
    public static final String EXPONENTIAL_FRAC_PROPERTY = "exponential.frac";
    public static final String EXPONENTIAL_FRAC_DEFAULT = "0.8571428571";  // 1/7

    /**
     * The exponential constant to use.
     */
    private double gamma;

    /******************************* Constructors **************************************/

    /**
     * Create an exponential generator with a mean arrival rate of
     * gamma.  (And half life of 1/gamma).
     */
    public ExponentialGenerator(double mean) {
        gamma = 1.0 / mean;
    }

    public ExponentialGenerator(double percentile, double range) {
        gamma = -Math.log(1.0 - percentile / 100.0) / range;  //1.0/mean;
    }

    /****************************************************************************************/

    public static void main(String[] args) {
        ExponentialGenerator e = new ExponentialGenerator(90, 100);
        int j = 0;
        for (int i = 0; i < 1000; i++) {
            if (e.nextValue() < 100) {
                j++;
            }
        }
        System.out.println("Got " + j + " hits.  Expect 900");
    }

    /**
     * Generate the next item as a long. This distribution will be skewed toward lower values; e.g. 0 will
     * be the most popular, 1 the next most popular, etc.
     *
     * @return The next item in the sequence.
     */
    @Override
    public Double nextValue() {
        return -Math.log(Utils.random().nextDouble()) / gamma;
    }

    @Override
    public double mean() {
        return 1.0 / gamma;
    }
}
