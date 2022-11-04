package fr.warzou.skyblock.cras.core.section.location;

import fr.warzou.skyblock.cras.ChunkRandomAccessSystem;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SectionLocation {

    public static final SectionLocation EMPTY = new SectionLocation(0, 0);

    protected final int start;
    protected final int end;

    public SectionLocation(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static SectionLocation toSectionLocation(byte[] bytes) {
        if (bytes.length != ChunkRandomAccessSystem.FREE_SECTION_LOCATION_SIZE)
            throw new IllegalArgumentException();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int start = buffer.getInt(0);
        int end = buffer.getInt(4);
        return new SectionLocation(start, end);
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[ChunkRandomAccessSystem.FREE_SECTION_LOCATION_SIZE];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putInt(0, this.start);
        buffer.putInt(4, this.end);
        return bytes;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "SectionLocation{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SectionLocation that)) return false;

        if (start != that.start) return false;
        return end == that.end;
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        return result;
    }
}
