package commandline.utils;

import java.util.HashMap;
import java.util.Map;

/* Class for storing variables */
public class Variables {
    private final Map<String, String> vars = new HashMap<>();

    public void putVar(String name, String value) {
        vars.put(name, value);
    }

    public String getVar(String name) {
        return vars.getOrDefault(name, " ");
    }
}
