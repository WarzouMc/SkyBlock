package fr.warzou.skyblock.cras.core.access;

import fr.warzou.skyblock.cras.ChunkRandomAccessSystem;
import fr.warzou.skyblock.cras.common.Element;
import fr.warzou.skyblock.cras.core.section.Section;
import fr.warzou.skyblock.cras.core.section.array.DataArray;
import fr.warzou.skyblock.cras.core.section.array.FreeArray;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.util.concurrent.ExecutionException;

public class RandomAccessSystem {

    private final AsynchronousFileChannel asynchronousFileChannel;
    private final int randomFileStart;
    private final DataArray data;
    private final FreeArray free;

    public RandomAccessSystem(AsynchronousFileChannel asynchronousFileChannel, int randomFileStart) {
        this.asynchronousFileChannel = asynchronousFileChannel;
        this.randomFileStart = randomFileStart;
        this.data = readDataArray();
        this.free = readFreeArray();
    }

    Section readSection(short index) {
        return null; //todo
    }

    Element readElement(short index) {
        return null; //todo
    }

    void writeElement(Element element) {
        //todo
    }

     void removeElement(Element element) {
        //todo
     }

     private void writeSection(Section section) {
        //todo
     }

    private DataArray readDataArray() {
        byte[] array = new byte[ChunkRandomAccessSystem.DATA_ARRAY_BYTES_COUNT];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        try {
            this.asynchronousFileChannel.read(buffer, this.randomFileStart).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return new DataArray(array);
    }

    private FreeArray readFreeArray() {
        byte[] array = new byte[ChunkRandomAccessSystem.FREE_ARRAY_BYTES_COUNT];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        try {
            this.asynchronousFileChannel.read(buffer, this.randomFileStart + ChunkRandomAccessSystem.DATA_ARRAY_BYTES_COUNT).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return new FreeArray(array);
    }
}
