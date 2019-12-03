package com.brentcroft.drawbridge.chain;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Chain< CONTEXT >
{

    private final List< Command< ? super CONTEXT > > commands;

    public Chain( List< Command< ? super CONTEXT > > commands )
    {
        this.commands = unmodifiableList( commands );
    }

    public CONTEXT executeUsing( CONTEXT context )
    {
        commands.forEach( command -> command.action( context ) );

        return context;
    }

    public static class CommandInitialiser< CONTEXT >
    {
        public Builder< CONTEXT > firstDo( Command< ? super CONTEXT > firstCommand )
        {
            return new Builder<>( firstCommand );
        }
    }

    private static class TypeInitialiser
    {
        public < CONTEXT > CommandInitialiser< CONTEXT > withContextType( Class< CONTEXT > contextType )
        {
            return new CommandInitialiser<>();
        }
    }

    public static < C > CommandInitialiser< C > of( Class< C > contextType )
    {
        return new TypeInitialiser().withContextType( contextType );
    }


    public static class Builder< CONTEXT >
    {

        private List< Command< ? super CONTEXT > > commands;

        private Builder( Command< ? super CONTEXT > firstCommand )
        {
            commands = new ArrayList<>();
            commands.add( firstCommand );
        }

        public Builder< CONTEXT > andThen( Command< ? super CONTEXT > command )
        {
            commands.add( command );
            return this;
        }

        public Builder< CONTEXT > andThen( Command< ? super CONTEXT > command, Guard< ? super CONTEXT > guard )
        {
            commands.add( new GuardedCommand<>( command, guard ) );
            return this;
        }

        public Chain< CONTEXT > build()
        {
            return new Chain<>( commands );
        }

    }
}
