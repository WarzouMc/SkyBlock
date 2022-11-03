package fr.warzou.skyblock.cras.v1mdr.core;

import fr.warzou.skyblock.cras.v1mdr.core.index.IndexType;
import fr.warzou.skyblock.cras.v1mdr.core.index.SectionIndex;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CompoundSectionIndex {

    private final int[] index = new int[0x800];
    private final List<SectionIndex> tags;
    private final Queue<SectionIndex> freeSpace;

    CompoundSectionIndex(LinkedList<SectionIndex> tags, ConcurrentLinkedDeque<SectionIndex> freeSpace) {
        this.tags = tags;
        this.freeSpace = freeSpace;
    }

    void pushFreeSection(SectionIndex newFreeSection) {
        if (newFreeSection.getType() == IndexType.FREE_SPACE || !this.tags.contains(newFreeSection))
            return;
        pushFreeSection0(newFreeSection);
    }

    SectionIndex allocateSection(int size) {
        SectionIndex allocate = this.freeSpace.poll();
        if (allocate == null)
            throw new IllegalStateException("No more free space !");

        int index = allocate.getIndex();
        SectionIndex[] array = allocate.split(size, 2, this::nextIndex);

        array[0].setType(IndexType.DATA_SPACE);
        tags.add(allocate);
        pushFreeSection0(array[1]);
        return array[0];
    }

    private void pushFreeSection0(SectionIndex section) {
        this.tags.remove(section);
        section.setType(IndexType.FREE_SPACE);
        this.freeSpace.add(section);
    }

    private int nextIndex() {
        int index = 0;
        while (containIndex(index))
            index++;
        return index;
    }

    private boolean containIndex(int index) {
        return tags.stream().map(SectionIndex::getIndex).anyMatch(i -> i == index);
    }
}
