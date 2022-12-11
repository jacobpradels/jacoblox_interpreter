package com.cecs497.lox;

import java.util.List;

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

