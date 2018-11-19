/**
 * This file is part of Craftconomy3.
 * <p>
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018, Pavog <http://github.com/pavog/>
 * <p>
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.tools.commands.bukkit;

import com.greatmancode.tools.commands.CommandHandler;
import com.greatmancode.tools.commands.PlayerCommandSender;
import com.greatmancode.tools.commands.SubCommand;
import com.greatmancode.tools.commands.interfaces.CommandReceiver;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BukkitCommandReceiver implements CommandReceiver, CommandExecutor {
    private CommandHandler commandHandler;

    public BukkitCommandReceiver(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        SubCommand subCommand = commandHandler.getCommand(command.getName());
        if (subCommand != null) {
            String subCommandValue = "";
            String[] newArgs;
            if (args.length <= 1) {
                newArgs = new String[0];
                if (args.length != 0) {
                    subCommandValue = args[0];
                }
            } else {
                newArgs = new String[args.length - 1];
                subCommandValue = args[0];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            }
            com.greatmancode.tools.commands.CommandSender sender;
            // Fetch command sender from bukkit
            if (commandSender instanceof ConsoleCommandSender) {
                // Command sent from console
                sender = new com.greatmancode.tools.commands.ConsoleCommandSender();
            } else if (commandSender instanceof Player) {
                // Command sent from a player
                sender = new PlayerCommandSender(commandSender.getName(), ((Player) commandSender).getUniqueId());
            } else if (commandSender instanceof BlockCommandSender) {
                // Command sent from commandblock
                sender = new com.greatmancode.tools.commands.BlockCommandSender();
            } else {
                // Something else, we don't know yet
                sender = (com.greatmancode.tools.commands.CommandSender) commandSender;
            }
            subCommand.execute(subCommandValue, sender, newArgs);
            return true;
        }
        return false;
    }
}
