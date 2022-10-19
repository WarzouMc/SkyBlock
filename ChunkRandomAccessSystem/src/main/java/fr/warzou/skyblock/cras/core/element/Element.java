package fr.warzou.skyblock.cras.core.element;

import fr.warzou.skyblock.cras.core.index.SectionIndex;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Element {

    private final Queue<SectionIndex> sections = new ConcurrentLinkedDeque<>();

}
