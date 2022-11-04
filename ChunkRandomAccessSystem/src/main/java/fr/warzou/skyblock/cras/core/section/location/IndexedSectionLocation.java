package fr.warzou.skyblock.cras.core.section.location;

import fr.warzou.skyblock.cras.ChunkRandomAccessSystem;

import java.nio.ByteBuffer;

public final class IndexedSectionLocation extends SectionLocation {

    public static final IndexedSectionLocation EMPTY = new IndexedSectionLocation((short) 0, 0, 0, false, 0);

    private final short index;
    private final boolean chunk;
    private final int chunkLocation;

    public IndexedSectionLocation(short index, int start, int end, boolean chunk, int chunkLocation) {
        super(start, end);
        this.index = index;
        this.chunk = chunk;
        this.chunkLocation = chunkLocation;
    }

    public static IndexedSectionLocation toSectionLocation(byte[] bytes) {
        if (bytes.length != ChunkRandomAccessSystem.SECTION_INDEX_SIZE)
            throw new IllegalArgumentException();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        short index = buffer.getShort(0);
        int start = buffer.getInt(2);
        int end = buffer.getInt(6);
        byte chunk = buffer.get(10);
        int chunkLocation = buffer.get(11);
        return new IndexedSectionLocation(index, start, end, chunk == 0x01, chunkLocation);
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = new byte[ChunkRandomAccessSystem.SECTION_INDEX_SIZE];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putShort(0, this.index);
        buffer.putInt(2, this.start);
        buffer.putInt(6, this.end);
        buffer.put(10, (byte) (this.chunk ? 0x01 : 0x00));
        buffer.putInt(11, this.chunkLocation);
        return bytes;
    }

    public short getIndex() {
        return this.index;
    }

    public boolean isChunk() {
        return this.chunk;
    }

    public int getChunkLocation() {
        return this.chunkLocation;
    }

    @Override
    public String toString() {
        return "IndexedSectionLocation{" +
                "index=" + index +
                ", chunk=" + chunk +
                ", chunkLocation=" + chunkLocation +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndexedSectionLocation that)) return false;
        if (!super.equals(o)) return false;

        if (index != that.index) return false;
        if (start != that.start) return false;
        if (end != that.end) return false;
        if (chunk != that.chunk) return false;
        return chunkLocation == that.chunkLocation;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) index;
        result = 31 * result + start;
        result = 31 * result + end;
        result = 31 * result + (chunk ? 1 : 0);
        result = 31 * result + chunkLocation;
        return result;
    }
}
