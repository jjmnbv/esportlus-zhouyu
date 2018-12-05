package com.kaihei.esportingplus.common.tools;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author  zhangfang
 */
public class ExpressJEval {
    static public Object calculate(String expressJ,Map<String,Object> params ){
        try {
            ScriptEngine se = new ScriptEngineManager().getEngineByName("js");
            Compilable ce = (Compilable) se;
            CompiledScript cs = ce.compile(expressJ);
            Bindings bindings = se.createBindings();
            for(String key:params.keySet()){
                bindings.put(key, params.get(key));
            }
            return cs.eval(bindings);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }
}
