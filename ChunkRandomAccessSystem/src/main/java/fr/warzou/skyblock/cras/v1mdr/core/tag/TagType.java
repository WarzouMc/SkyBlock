package fr.warzou.skyblock.cras.v1mdr.core.tag;

public enum TagType {

    END(false), // tag 00 value 0000
    NEXT(true), // tag 01 value between 0x0000 and 0xFFFF (include)
    ;

    // value size = u2
    private final boolean valuable;

    TagType(boolean valuable) {
        this.valuable = valuable;
    }

    public int getTagInt() {
        return this.ordinal();
    }

    public boolean isValuable() {
        return this.valuable;
    }
}
