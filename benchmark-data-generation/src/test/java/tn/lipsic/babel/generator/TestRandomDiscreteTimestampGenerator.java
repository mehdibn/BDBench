package tn.lipsic.babel.generator;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TestRandomDiscreteTimestampGenerator {

    @Test
    public void systemTime() throws Exception {
        final RandomDiscreteTimestampGenerator generator =
                new RandomDiscreteTimestampGenerator(60, TimeUnit.SECONDS, 60);
        List<Long> generated = Lists.newArrayList();
        for (int i = 0; i < 60; i++) {
            generated.add(generator.nextValue());
        }
        assertEquals(generated.size(), 60);
        try {
            generator.nextValue();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void withStartTime() throws Exception {
        final RandomDiscreteTimestampGenerator generator =
                new RandomDiscreteTimestampGenerator(60, TimeUnit.SECONDS, 1072915200L, 60);
        List<Long> generated = Lists.newArrayList();
        for (int i = 0; i < 60; i++) {
            generated.add(generator.nextValue());
        }
        assertEquals(generated.size(), 60);
        Collections.sort(generated);
        long ts = 1072915200L - 60; // starts 1 interval in the past
        for (final long t : generated) {
            assertEquals(t, ts);
            ts += 60;
        }
        try {
            generator.nextValue();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void tooLarge() throws Exception {
        new RandomDiscreteTimestampGenerator(60, TimeUnit.SECONDS,
                RandomDiscreteTimestampGenerator.MAX_INTERVALS + 1);
    }

    //TODO - With PowerMockito we could UT the initializeTimestamp(long) call.
    // Otherwise it would involve creating more functions and that would get ugly.
}
