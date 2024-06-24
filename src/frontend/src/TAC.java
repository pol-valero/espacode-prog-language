package frontend.src;

import java.util.HashMap;
import java.util.LinkedList;

public class TAC {

    private HashMap<String, LinkedList<TACBasicBloc>> tacBlocs;

    public TAC() {
        tacBlocs = new HashMap<>();
    }

    public void addBasicBloc(String function, TACBasicBloc basicBloc) {
        if (tacBlocs.containsKey(function)) {
            tacBlocs.get(function).add(basicBloc);
        } else {
            LinkedList<TACBasicBloc> list = new LinkedList<>();
            list.add(basicBloc);
            tacBlocs.put(function, list);
        }
    }

    public HashMap<String, LinkedList<TACBasicBloc>> getTacBlocs() {
        return tacBlocs;
    }



}
