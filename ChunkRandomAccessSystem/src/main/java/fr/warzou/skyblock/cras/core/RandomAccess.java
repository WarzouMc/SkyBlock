package fr.warzou.skyblock.cras.core;

import fr.warzou.skyblock.cras.core.index.IndexTable;
import fr.warzou.skyblock.cras.core.index.SectionIndex;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class RandomAccess {

    private final File output;
    private final int start;
    private final AsynchronousFileChannel asyncFileChanel;
    private final IndexTable indexTable;

    public RandomAccess(File output, int start) throws ExecutionException, InterruptedException, IOException {
        this.output = output;
        this.start = start;
        this.asyncFileChanel = AsynchronousFileChannel.open(this.output.toPath(), StandardOpenOption.WRITE, StandardOpenOption.READ);
        this.indexTable = readIndexTable();
    }

    public void write(byte[] data) {

    }

    public File getOutput() {
        return this.output;
    }

    public IndexTable getIndexTable() {
        return this.indexTable;
    }

    private Optional<SectionIndex> findFreeSection() {

        return null;
    }

    private IndexTable readIndexTable() throws ExecutionException, InterruptedException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(IndexTable.INDEX_ARRAY_SIZE);
        this.asyncFileChanel.read(byteBuffer, this.start).get();
        return new IndexTable(byteBuffer.array(), start);
    }
}
