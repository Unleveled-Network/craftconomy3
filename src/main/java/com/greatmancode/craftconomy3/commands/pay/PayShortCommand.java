/*
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018-2019, Pavog <http://github.com/pavog/>
 *
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3. If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.commands.pay;

import com.greatmancode.craftconomy3.tools.commands.CommandHandler;
import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.commands.SubCommand;
import com.greatmancode.craftconomy3.tools.commands.interfaces.Command;

public class PayShortCommand extends SubCommand {

    public PayShortCommand(String name, CommandHandler commandHandler, SubCommand parent, int level) {
        super(name, commandHandler, parent, level);
    }

    @Override
    public boolean commandExist(String name) {
        return true;
    }

    @Override
    public void execute(String commandName, CommandSender sender, String[] args) {
        if (level <= commandHandler.getCurrentLevel()) {
            if (commandExist(commandName)) {
                Command command = commandList.get("");
                if (command == null) {
                    commandHandler.getServerCaller().getPlayerCaller().sendMessage(sender, "Command Executing : " + this.getName() + " : " + commandName + " is null", getName());
                } else {
                    // Add command name to the arguments so that the command is getting executed correctly.
                    String[] newArgs = new String[args.length + 1];
                    newArgs[0] = commandName;
                    System.arraycopy(args, 0, newArgs, 1, args.length);

                    this.executeCommand(command, sender, newArgs);
                }
            }
        } else {
            commandHandler.getServerCaller().getPlayerCaller().sendMessage(sender, commandHandler.getWrongLevelMsg(), getName());
        }
    }
}
