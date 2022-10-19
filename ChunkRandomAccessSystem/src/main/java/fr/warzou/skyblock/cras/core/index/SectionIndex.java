package fr.warzou.skyblock.cras.core.index;

public class SectionIndex {

    public static final byte INDEX_SIZE = 4;
    public static final byte START_SIZE = 4;
    public static final byte END_SIZE = 4;

    private final IndexType type;
    protected int index;
    private final int start;
    private final int end;
    private final int size;

    public SectionIndex(int index, int start, int end) {
        this(IndexType.DATA_SPACE, index, start, end);
    }

    public SectionIndex(IndexType type, int index, int start, int end) {
        this.type = type;
        this.index = index;
        this.start = start;
        this.end = end;
        this.size = this.end - this.start;
    }

    public static SectionIndex emptySection(int start, int end) {
        return new SectionIndex(IndexType.FREE_SPACE, -1, start, end);
    }

    public SectionIndex[] split(int size, NextIndex nextIndex) {
        return split(size, 0, nextIndex);
    }

    public SectionIndex[] split(int size, int count, NextIndex nextIndex) {
        size = Math.abs(size);
        count = Math.abs(count);

        if (count == 0)
            return maxSplit(size, nextIndex);

        return split0(size, count, nextIndex);
    }

    public IndexType getType() {
        return this.type;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public int getSize() {
        return this.size;
    }

    private SectionIndex[] maxSplit(int newSize, NextIndex nextIndex) {
        int splitCount = this.size / newSize;
        int remindSize = this.size % newSize;

        SectionIndex[] array = new SectionIndex[splitCount + 1];
        for (int i = 0; i < splitCount; i++)
            array[i] = new SectionIndex(this.type, nextIndex.next(), this.start + newSize * i, this.start + newSize * (i + 1));

        array[splitCount] = new SectionIndex(this.type, nextIndex.next(), this.end - remindSize, this.end);
        return array;
    }

    private SectionIndex[] split0(int newSize, int count, NextIndex nextIndex) {
        if (this.size / newSize > count)
            return maxSplit(newSize, nextIndex);

        SectionIndex[] array = new SectionIndex[count];
        for (int i = 0; i < count - 1; i ++)
            array[i] = new SectionIndex(this.type, nextIndex.next(), this.start + (newSize * i), this.end + (newSize * (i + 1)));

        array[count - 1] = new SectionIndex(this.type, nextIndex.next(), this.start + (newSize * count), this.end);
        return array;
    }
}
