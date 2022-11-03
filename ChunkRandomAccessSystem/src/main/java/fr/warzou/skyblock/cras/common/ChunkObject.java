package fr.warzou.skyblock.cras.common;

public class ChunkObject {

    private final int location;
    private final byte[] chunk;

    public ChunkObject(int location, byte[] chunk) {
        this.location = location;
        this.chunk = chunk;
    }

    public int getLocation() {
        return this.location;
    }

    public byte[] getChunk() {
        return this.chunk;
    }
}
