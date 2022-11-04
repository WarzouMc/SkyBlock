package fr.warzou.skyblock.cras.core.access;

import fr.warzou.skyblock.cras.common.ChunkObject;

import java.nio.channels.AsynchronousFileChannel;

public class Accessor {

    private final AsynchronousFileChannel asynchronousFileChannel;
    private final int randomFileStart;
    private final RandomAccessSystem system;

    public Accessor(AsynchronousFileChannel asynchronousFileChannel, int randomFileStart) {
        this.asynchronousFileChannel = asynchronousFileChannel;
        this.randomFileStart = randomFileStart;
        this.system = new RandomAccessSystem(this.asynchronousFileChannel, randomFileStart);
    }

    public ChunkObject getChunk(int location) {
        return null; //todo
    }

    public void addChunk(int location, byte[] chunk) {
        //todo
    }

    public void removeChunk(int location) {
        //todo
    }
}
