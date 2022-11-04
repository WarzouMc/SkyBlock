package fr.warzou.skyblock.cras.core.section.array;

import fr.warzou.skyblock.cras.ChunkRandomAccessSystem;
import fr.warzou.skyblock.cras.core.section.location.IndexedSectionLocation;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

public class DataArray implements Iterable<IndexedSectionLocation> {

    public static final DataArray EMPTY = new DataArray();

    private final IndexedSectionLocation[] indices = new IndexedSectionLocation[ChunkRandomAccessSystem.DATA_ARRAY_SIZE];

    private DataArray() {
        Arrays.fill(indices, IndexedSectionLocation.EMPTY);
    }

    public DataArray(byte[] array) {
        if (array.length != ChunkRandomAccessSystem.DATA_ARRAY_BYTES_COUNT)
            throw new IllegalArgumentException();

        int sectionIndexSize = ChunkRandomAccessSystem.SECTION_INDEX_SIZE;
        for (int i = 0; i < this.indices.length; i++) {
            byte[] section = Arrays.copyOfRange(new byte[sectionIndexSize], i * sectionIndexSize, (i + 1) * sectionIndexSize);
            this.indices[i] = IndexedSectionLocation.toSectionLocation(section);
        }
    }

    @NotNull
    @Override
    public Iterator<IndexedSectionLocation> iterator() {
        return Arrays.stream(this.indices).iterator();
    }
}
