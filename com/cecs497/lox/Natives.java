package com.cecs497.lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Clock implements LoxCallable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return (double)System.currentTimeMillis() / 1000.0;
    }
    @Override
    public String toString() {
        return "<native fn>";
    }  
}

class Trig implements LoxCallable {

    // 1 - Cos
    // 2 - Sin
    // 3 - Tan
    // 4 - Secant
    // 5 - Cosecant
    // 6 - Cotangent
    private int fn;
    public Trig(int fn) {
        this.fn = fn;
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        switch (fn) {
            case 1: return (double)Math.cos((double)arguments.get(0));
            case 2: return (double)Math.sin((double)arguments.get(0));
            case 3: return (double)Math.tan((double)arguments.get(0));
            case 4: return (double)Math.acos((double)arguments.get(0));
            case 5: return (double)Math.asin((double)arguments.get(0));
            default: return (double)Math.atan((double)arguments.get(0));
        }
    }
    @Override
    public String toString() {
        return "<native fn>";
    }
}

class Memory implements LoxCallable {

    public static Map<Integer,Object> memory = new HashMap<>();

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        double start_d = (Double)arguments.get(0);
        double end_d = (Double)arguments.get(1);
        int start = (int)start_d;
        int end = (int)end_d;
        return new MemoryController(start,end);
    }
}

class MemoryController implements LoxCallable {

    private int start;
    private int end;

    MemoryController(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int arity() {
        return 3;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        String mode = (String)arguments.get(0);
        double location_d = (Double)arguments.get(1);
        int location = (int)location_d;
        if (mode.equals("r")) {
            if (location < this.start || location >= this.end) {
                report("Segmentation fault. Invalid memory access in read mode.");
            }
            Object data = Memory.memory.get(location);
            if (data instanceof Double) {
                return (Double)data;
            } else {
                return (String)data;
            }
        } else if (mode.equals("w")) {
            if (location < this.start || location >= this.end) {
                report("Segmentation fault. Invalid memory access in write mode.");
            }
            Object data = arguments.get(2);
            Memory.memory.put(location,data);
            return null;
        } else {
            report("Invalid memory access mode.");
            return null;
        }
    }
    public static void report(String message) {
        Token t = new Token(TokenType.NATIVE, "", null, 0,"");
            throw new RuntimeError(t,message);
    }
}
