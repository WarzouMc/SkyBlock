package fr.warzou.skyblock.cras.common;

import fr.warzou.skyblock.cras.core.section.Section;

import java.util.List;

public class Element {

    private final List<Section> sections;

    public Element(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return this.sections;
    }
}
