package fr.warzou.skyblock.cras.core.section.location;

public class SectionLocation {

    protected final int start;
    protected final int end;

    public SectionLocation(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }
}
