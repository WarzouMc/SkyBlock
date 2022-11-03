package fr.warzou.skyblock.cras.core.section;

public class Section {
    private final int size;
    private final boolean hasNext;
    private final short next;

    public Section(int size, boolean hasNext, short next) {
        this.size = size;
        this.hasNext = hasNext;
        this.next = next;
    }

    public int getSize() {
        return this.size;
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    public short getNext() {
        return this.next;
    }
}
