package frontend.src;

import java.util.LinkedList;

public class TACBasicBloc {

    private LinkedList<TACBBentry> tacBlockEntries;

    public TACBasicBloc() {
        tacBlockEntries = new LinkedList<>();
    }

    public void addEntry(TACBBentry entry) {
        tacBlockEntries.add(entry);
    }

    public LinkedList<TACBBentry> getTacBlockEntries() {
        return tacBlockEntries;
    }


}
