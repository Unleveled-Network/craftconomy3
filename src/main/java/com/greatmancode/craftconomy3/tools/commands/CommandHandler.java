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
package com.greatmancode.craftconomy3.tools.commands;

import com.greatmancode.craftconomy3.tools.caller.bukkit.BukkitServerCaller;
import com.greatmancode.craftconomy3.tools.commands.bukkit.BukkitCommandReceiver;
import com.greatmancode.craftconomy3.tools.commands.interfaces.CommandReceiver;
import com.greatmancode.craftconomy3.tools.interfaces.caller.ServerCaller;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    @Getter
    private ServerCaller serverCaller;
    @Getter
    private CommandReceiver commandReceiver;

    private Map<String, SubCommand> commandList = new HashMap<>();
    @Setter
    @Getter
    private int currentLevel = 0;
    @Getter
    @Setter
    private String wrongLevelMsg = "Wrong level!";

    public CommandHandler(ServerCaller serverCaller) {
        this.serverCaller = serverCaller;
        if (this.serverCaller instanceof BukkitServerCaller) {
            commandReceiver = new BukkitCommandReceiver(this);
        }
        serverCaller.getLoader().setCommandReceiver(commandReceiver);
    }

    public CommandHandler registerMainCommand(String name, SubCommand subCommand) {
        commandList.put(name, subCommand);
        serverCaller.addCommand(name, "", subCommand);
        return this;
    }

    public SubCommand getCommand(String name) {
        return commandList.get(name);
    }
}
