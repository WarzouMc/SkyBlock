package fr.warzou.skyblock.cras.core.access;

import fr.warzou.skyblock.cras.common.Element;
import fr.warzou.skyblock.cras.core.section.Section;
import fr.warzou.skyblock.cras.core.section.array.DataArray;
import fr.warzou.skyblock.cras.core.section.array.FreeArray;

import java.io.OutputStream;
import java.nio.channels.AsynchronousFileChannel;

public class RandomAccessSystem {

    private final AsynchronousFileChannel asynchronousFileChannel;
    private final DataArray data;
    private final FreeArray free;

    public RandomAccessSystem(AsynchronousFileChannel asynchronousFileChannel) {
        this.asynchronousFileChannel = asynchronousFileChannel;
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
        //todo
        return null;
    }

    private FreeArray readFreeArray() {
        //todo
        return null;
    }
}
