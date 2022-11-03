package fr.warzou.skyblock.cras.v1mdr.core.element;

import fr.warzou.skyblock.cras.v1mdr.core.index.SectionIndex;

import java.util.LinkedList;
import java.util.List;

// todo may be package-private
public class Element {

    private final List<SectionIndex> element = new LinkedList<>();

    public void fill(List<SectionIndex> list) {
        //todo
    }

    public void grow(int size) {
        // todo
    }

    public int size() {
        return element.stream().mapToInt(SectionIndex::getSize).sum();
    }
}
