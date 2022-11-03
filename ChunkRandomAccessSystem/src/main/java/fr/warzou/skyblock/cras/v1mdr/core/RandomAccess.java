package fr.warzou.skyblock.cras.v1mdr.core;

import fr.warzou.skyblock.cras.v1mdr.core.element.Element;
import fr.warzou.skyblock.cras.v1mdr.core.index.IndexTable;
import fr.warzou.skyblock.cras.v1mdr.core.index.IndexType;
import fr.warzou.skyblock.cras.v1mdr.core.index.SectionIndex;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;

public class RandomAccess {

    private final File output;
    private final int start;
    private final AsynchronousFileChannel asyncFileChanel;
    private final IndexTable indexTable;
    private final CompoundSectionIndex compoundSectionIndex;

    public RandomAccess(File output, int start) throws ExecutionException, InterruptedException, IOException {
        this.output = output;
        this.start = start;
        this.asyncFileChanel = AsynchronousFileChannel.open(this.output.toPath(), StandardOpenOption.WRITE, StandardOpenOption.READ);
        this.indexTable = readIndexTable();
        this.compoundSectionIndex = new CompoundSectionIndex(listDataSpace(), listFreeSpace());
    }

    public void write(byte[] data) {
        // todo
    }

    public File getOutput() {
        return this.output;
    }

    public IndexTable getIndexTable() {
        return this.indexTable;
    }

    private Optional<SectionIndex> findFreeSection() {
        return Optional.empty();
    }

    private Element createElement(int size) {
        //todo
        return null;
    }

    private LinkedList<SectionIndex> listDataSpace() {
        LinkedList<SectionIndex> dataSpace = new LinkedList<>();
        for (SectionIndex sectionIndex : this.indexTable)
            if (sectionIndex.getType() == IndexType.DATA_SPACE) dataSpace.add(sectionIndex);
        return dataSpace;
    }

    private ConcurrentLinkedDeque<SectionIndex> listFreeSpace() {
        ConcurrentLinkedDeque<SectionIndex> freeSpace = new ConcurrentLinkedDeque<>();
        for (SectionIndex sectionIndex : this.indexTable)
            if (sectionIndex.getType() == IndexType.FREE_SPACE) freeSpace.push(sectionIndex);
        return freeSpace;
    }

    private IndexTable readIndexTable() throws ExecutionException, InterruptedException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(IndexTable.INDEX_ARRAY_SIZE);
        this.asyncFileChanel.read(byteBuffer, this.start).get();
        return new IndexTable(byteBuffer.array(), start);
    }
}
