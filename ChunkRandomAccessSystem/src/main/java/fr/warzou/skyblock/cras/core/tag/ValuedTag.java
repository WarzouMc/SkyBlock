package fr.warzou.skyblock.cras.core.tag;

import fr.warzou.skyblock.cras.core.index.SectionIndex;

import java.util.Optional;

/**
 * Valued tag is a tag go to the end of a section to specify were we go to finish the read
 */
public class ValuedTag {

    private final TagType type;
    private final Optional<SectionIndex> nextSection;

    public static final ValuedTag END_TAG = new ValuedTag();

    public ValuedTag() {
        this(TagType.END, Optional.empty());
    }

    public ValuedTag(TagType type, Optional<SectionIndex> nextSection) {
        this.type = type;
        this.nextSection = nextSection;
    }

    public TagType getType() {
        return this.type;
    }

    public Optional<SectionIndex> getNextSection() {
        return this.nextSection;
    }
}
