package fr.warzou.skyblock.cras.core.section.array;

import fr.warzou.skyblock.cras.ChunkRandomAccessSystem;
import fr.warzou.skyblock.cras.core.section.location.SectionLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class FreeArray implements Iterable<SectionLocation> {

    private final SectionLocation[] indices = new SectionLocation[ChunkRandomAccessSystem.FREE_ARRAY_SIZE];

    @NotNull
    @Override
    public Iterator<SectionLocation> iterator() {
        return null; //todo
    }
}
