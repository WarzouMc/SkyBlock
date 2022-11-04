package fr.warzou.skyblock.cras.core.section.array;

import fr.warzou.skyblock.cras.ChunkRandomAccessSystem;
import fr.warzou.skyblock.cras.core.section.location.SectionLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class FreeArray implements Iterable<SectionLocation> {

    public static final FreeArray EMPTY = new FreeArray();

    private final SectionLocation[] indices = new SectionLocation[ChunkRandomAccessSystem.FREE_ARRAY_SIZE];

    private FreeArray() {
        Arrays.fill(indices, SectionLocation.EMPTY);
    }

    public FreeArray(byte[] array) {
        if (array.length != ChunkRandomAccessSystem.FREE_ARRAY_BYTES_COUNT)
            throw new IllegalArgumentException();

        int sectionLocationSize = ChunkRandomAccessSystem.FREE_SECTION_LOCATION_SIZE;
        for (int i = 0; i < this.indices.length; i++) {
            byte[] section = Arrays.copyOfRange(new byte[sectionLocationSize], i * sectionLocationSize, (i + 1) * sectionLocationSize);
            this.indices[i] = SectionLocation.toSectionLocation(section);
        }
    }

    @NotNull
    @Override
    public Iterator<SectionLocation> iterator() {
        return null; //todo
    }
}
