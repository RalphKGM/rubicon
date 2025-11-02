package com.gabriel.drawfx.command;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
public class CommandService {
    static Stack<Command> undoStack = new Stack<Command>();
    static Stack<Command> redoStack = new Stack<Command>();

    public static void ExecuteCommand(Command command) {
        
        redoStack.clear();
        System.out.println("[CommandService] ExecuteCommand: " + command.getClass().getSimpleName());
        command.execute();
        undoStack.push(command);
        notifyListeners();
    }

    public static void undo() {
        if (undoStack.empty())
            return;
        Command command = undoStack.pop();
        System.out.println("[CommandService] undo: " + command.getClass().getSimpleName());
        command.undo();
        redoStack.push(command);
        notifyListeners();
    }

    public static void redo() {
        if (redoStack.empty())
            return;
        Command command = redoStack.pop();
        System.out.println("[CommandService] redo: " + command.getClass().getSimpleName());
        command.execute();
        undoStack.push(command);
        notifyListeners();
    }

    
    private static final List<Runnable> listeners = new ArrayList<>();

    public static void addListener(Runnable r) {
        if (r != null) listeners.add(r);
    }

    public static void removeListener(Runnable r) {
        listeners.remove(r);
    }

    private static void notifyListeners() {
        for (Runnable r : new ArrayList<>(listeners)) {
            try {
                r.run();
            } catch (Exception ex) {
                
                ex.printStackTrace();
            }
        }
    }
}