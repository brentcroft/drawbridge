package com.brentcroft.drawbridge.chain;

public class GuardedCommand<CONTEXT>
implements Command<CONTEXT> {

    private final Guard<? super CONTEXT>   guard;
    private final Command<? super CONTEXT> command;

    public GuardedCommand(Command<? super CONTEXT> command,
                          Guard<? super CONTEXT>   guard) {
        this.guard   = guard;
        this.command = command;
    }

    @Override
    public void action(CONTEXT context) {
        if (guard.isSatisfied(context)) {
            command.action(context);
        }
    }
}
