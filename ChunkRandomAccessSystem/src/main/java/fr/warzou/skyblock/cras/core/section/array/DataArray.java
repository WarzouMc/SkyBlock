package fr.warzou.skyblock.cras.core.section.array;

import fr.warzou.skyblock.cras.ChunkRandomAccessSystem;
import fr.warzou.skyblock.cras.core.section.location.IndexedSectionLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class DataArray implements Iterable<IndexedSectionLocation> {

    private final IndexedSectionLocation[] indices = new IndexedSectionLocation[ChunkRandomAccessSystem.DATA_ARRAY_SIZE];

    @NotNull
    @Override
    public Iterator<IndexedSectionLocation> iterator() {
        return null; //todo
    }
}
