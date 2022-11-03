package fr.warzou.skyblock.cras.v1mdr.core.index;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexTable implements Iterable<SectionIndex> {

    public static final int SECTION_INDEX_SIZE = SectionIndex.INDEX_SIZE + SectionIndex.START_SIZE + SectionIndex.END_SIZE;
    public static final int INDEX_ARRAY_SIZE = SECTION_INDEX_SIZE * 0x800;

    private final byte[] indices;
    private final int start;
    private final int end;

    public IndexTable(byte[] indices, int start) {
        if (indices.length != INDEX_ARRAY_SIZE)
            throw new IllegalArgumentException(String.format("Indices size not allowed : %s != %s", indices.length, INDEX_ARRAY_SIZE));

        this.indices = indices;
        this.start = start;
        this.end = this.start + INDEX_ARRAY_SIZE;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @NotNull
    @Override
    public Iterator<SectionIndex> iterator() {
        return new IndexIterator(this);
    }

    private static class IndexIterator implements Iterator<SectionIndex> {

        private final IndexTable table;
        private final byte[] indices;
        private int currentRawIndex = 0;

        private IndexIterator(IndexTable table) {
            this.table = table;
            this.indices = table.indices;
        }

        @Override
        public boolean hasNext() {
            return currentRawIndex + 10 < INDEX_ARRAY_SIZE;
        }

        @Override
        public SectionIndex next() {
            if (!hasNext())
                throw new NoSuchElementException();
            SectionIndex sectionIndex = read();
            return read();
        }

        private @NotNull SectionIndex read() {
            int index = ByteBuffer.wrap(this.indices, this.currentRawIndex += SectionIndex.INDEX_SIZE, SectionIndex.INDEX_SIZE).getInt();
            int start = ByteBuffer.wrap(this.indices, this.currentRawIndex += SectionIndex.START_SIZE, SectionIndex.START_SIZE).getInt();
            int end = ByteBuffer.wrap(this.indices, this.currentRawIndex += SectionIndex.END_SIZE, SectionIndex.END_SIZE).getInt();
            return new SectionIndex(this.table, start, end, index);
        }
    }
}
