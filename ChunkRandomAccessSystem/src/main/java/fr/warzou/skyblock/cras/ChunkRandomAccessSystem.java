package fr.warzou.skyblock.cras;

import fr.warzou.skyblock.cras.common.ChunkObject;
import fr.warzou.skyblock.cras.core.access.Accessor;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.StandardOpenOption;

public class ChunkRandomAccessSystem {

    public static final int DATA_ARRAY_SIZE = 2048;
    public static final int FREE_ARRAY_SIZE = 2048;
    public static final int SECTION_INDEX_SIZE = 15;
    public static final int FREE_SECTION_LOCATION_SIZE = 8;
    public static final int DATA_ARRAY_BYTES_COUNT = DATA_ARRAY_SIZE * SECTION_INDEX_SIZE;
    public static final int FREE_ARRAY_BYTES_COUNT = FREE_ARRAY_SIZE * FREE_SECTION_LOCATION_SIZE;

    private final File chunkFile;
    private final int randomFileStart;
    private final Accessor accessor;

    public ChunkRandomAccessSystem(File chunkFile, int randomFileStart) throws IOException {
        this.chunkFile = chunkFile;
        this.randomFileStart = randomFileStart;
        this.accessor = new Accessor(AsynchronousFileChannel.open(this.chunkFile.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE),
                randomFileStart);
    }

    public static byte[] createEmptyTables(String name) {
        return new byte[DATA_ARRAY_BYTES_COUNT + FREE_ARRAY_BYTES_COUNT];
    }

    public void addChunk(ChunkObject chunk) {
        this.accessor.addChunk(chunk.getLocation(), chunk.getChunk());
    }

    public ChunkObject getChunk(short x, short z) {
        return this.accessor.getChunk(toLocation(x, z));
    }

    public void removeChunk(short x, short z) {
        this.accessor.removeChunk(toLocation(x, z));
    }

    public File getChunkFile() {
        return this.chunkFile;
    }

    public int getRandomFileStart() {
        return this.randomFileStart;
    }

    private int toLocation(short x, short z) {
        return (x << 16) & z;
    }
}
