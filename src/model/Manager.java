package model;

import model.entities.*;
import model.relations.Call;
import model.relations.Interrupt;
import model.relations.Operation;
import model.relations.Transition;

import java.util.LinkedList;

public class Manager {

    private static LinkedList<Action> actions = new LinkedList<>();
    private static LinkedList<Interface> ifaces = new LinkedList<>();
    private static LinkedList<Peripheral> peripherals = new LinkedList<>();
    private static LinkedList<State> states = new LinkedList<>();
    private static LinkedList<TAD> tads = new LinkedList<>();
    private static LinkedList<Variable> variables = new LinkedList<>();

    private static LinkedList<Call> calls = new LinkedList<>();
    private static LinkedList<Interrupt> interrupts = new LinkedList<>();
    private static LinkedList<Operation> operations = new LinkedList<>();
    private static LinkedList<Transition> transitions = new LinkedList<>();

    public static Action createAction(int x, int y) {
        Action a = new Action(actions.size(), x, y);
        actions.add(a);
        return a;
    }

    public static Interface createInterface(int x, int y) {
        Interface i = new Interface(ifaces.size(), x, y);
        ifaces.add(i);
        return i;
    }

    public static Peripheral createPeripheral(int x, int y) {
        Peripheral p = new Peripheral(tads.size(), x, y);
        peripherals.add(p);
        return p;
    }

    public static State createState(int x, int y) {
        State s = new State(states.size(), x, y);
        states.add(s);
        return s;
    }

    public static TAD createTAD(int x, int y) {
        TAD t = new TAD(tads.size(), x, y);
        tads.add(t);
        return t;
    }

    public static Variable createVariable(int x, int y) {
        Variable v = new Variable(variables.size(), x, y);
        variables.add(v);
        return v;
    }

    public static Call createCall(int id, int x, int y, int x2, int y2, Entity origin, Entity destination) {
        Call c = new Call(calls.size(), x, y, x2, y2, origin, destination);
        calls.add(c);
        return c;
    }

    public static Interrupt createInterrupt(int id, int x, int y, int x2, int y2, Entity origin, Entity destination) {
        Interrupt i = new Interrupt(interrupts.size(), x, y, x2, y2, origin, destination);
        interrupts.add(i);
        return i;
    }

    public static Operation createOperation(int id, int x, int y, int x2, int y2, Entity origin, Entity destination) {
        Operation o = new Operation(operations.size(), x, y, x2, y2, origin, destination);
        operations.add(o);
        return o;
    }

    public static Transition createTransition(int id, int x, int y, int x2, int y2, Entity origin, Entity destination) {
        Transition t = new Transition(transitions.size(), x, y, x2, y2, origin, destination);
        transitions.add(t);
        return t;
    }
}
