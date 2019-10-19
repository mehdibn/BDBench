package tn.lipsic.babel;

/**
 * A byte iterator that handles encoding and decoding numeric values.
 * Currently this iterator can handle 64 bit signed values and double precision
 * floating point values.
 */
public class NumericByteIterator extends ByteIterator {
    private final byte[] payload;
    private final boolean floatingPoint;
    private int off;

    public NumericByteIterator(final long value) {
        floatingPoint = false;
        payload = Utils.longToBytes(value);
        off = 0;
    }

    public NumericByteIterator(final double value) {
        floatingPoint = true;
        payload = Utils.doubleToBytes(value);
        off = 0;
    }

    @Override
    public boolean hasNext() {
        return off < payload.length;
    }

    @Override
    public byte nextByte() {
        return payload[off++];
    }

    @Override
    public long bytesLeft() {
        return payload.length - off;
    }

    @Override
    public void reset() {
        off = 0;
    }

    public long getLong() {
        if (floatingPoint) {
            throw new IllegalStateException("Byte iterator is of the type double");
        }
        return Utils.bytesToLong(payload);
    }

    public double getDouble() {
        if (!floatingPoint) {
            throw new IllegalStateException("Byte iterator is of the type long");
        }
        return Utils.bytesToDouble(payload);
    }

    public boolean isFloatingPoint() {
        return floatingPoint;
    }

}