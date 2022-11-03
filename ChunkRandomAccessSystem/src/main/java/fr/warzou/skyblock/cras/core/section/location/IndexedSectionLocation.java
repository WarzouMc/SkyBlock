package fr.warzou.skyblock.cras.core.section.location;

public final class IndexedSectionLocation extends SectionLocation {

    private final short index;
    private final boolean chunk;
    private final int chunkLocation;

    public IndexedSectionLocation(short index, int start, int end, boolean chunk, int chunkLocation) {
        super(start, end);
        this.index = index;
        this.chunk = chunk;
        this.chunkLocation = chunkLocation;
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
}
