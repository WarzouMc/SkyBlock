package fr.warzou.skyblock.cras.core;

import fr.warzou.skyblock.cras.core.index.SectionIndex;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CompoundSectionIndex {

    private final List<SectionIndex> tags = new LinkedList<>();
    private final Queue<SectionIndex> freeSpace = new ConcurrentLinkedDeque<>();



    protected void pushFreeSection(SectionIndex newFreeSection) {
        this.freeSpace.add(newFreeSection);
    }

    protected SectionIndex allocateSection(int size) {

        return this.freeSpace.poll();
    }
}
