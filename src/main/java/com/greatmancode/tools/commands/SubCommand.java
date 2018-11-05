/**
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018, Pavog <http://github.com/pavog/>
 *
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.tools.commands;

import com.greatmancode.tools.commands.interfaces.Command;
import com.greatmancode.tools.commands.interfaces.CommandExecutor;
import lombok.Getter;

import java.util.*;

public class SubCommand implements Command {
    private Map<String, Command> commandList = new HashMap<String, Command>();
    private CommandHandler commandHandler;
    private SubCommand parent;
    @Getter
    private String name;
    private int level;

    public SubCommand(String name, CommandHandler commandHandler, SubCommand parent, int level) {
        this.name = name;
        this.commandHandler = commandHandler;
        this.parent = parent;
        this.level = level;
    }

    public SubCommand(String name, CommandHandler commandHandler, SubCommand parent) {
        this(name, commandHandler, parent, 0);
    }

    public void addCommand(String name, Command command) {
        commandList.put(name, command);
        if (command instanceof CommandExecutor) {
            commandHandler.getServerCaller().registerPermission(((CommandExecutor) command).getPermissionNode());
        }
    }

    public boolean commandExist(String name) {
        return commandList.containsKey(name);
    }

    public void execute(String command, CommandSender sender, String[] args) {
        if (level <= commandHandler.getCurrentLevel()) {
            if (commandExist(command)) {
                Command entry = commandList.get(command);
                if (entry instanceof CommandExecutor) {
                    CommandExecutor cmd = ((CommandExecutor) entry);
                    if(sender instanceof ConsoleCommandSender){
                        if(cmd.playerOnly()){
                            commandHandler.getServerCaller().getPlayerCaller().sendMessage(sender.toString(), "{{DARK_RED}}Only players can do this command!");
                        }
                    }else if (sender instanceof PlayerCommandSender){
                        if(commandHandler.getServerCaller().getPlayerCaller().checkPermission(((PlayerCommandSender)
                                sender).getUuid(),cmd.getPermissionNode())){
                            if (args.length >= cmd.minArgs() && args.length <= cmd.maxArgs()) {
                                cmd.execute(sender, args);
                            } else {
                                commandHandler.getServerCaller().getPlayerCaller().sendMessage(((PlayerCommandSender) sender).getUuid(), cmd.help());
                            }
                        }

                    }
                } else if (entry instanceof SubCommand) {
                    SubCommand subCommand = (SubCommand) entry;

                    String subSubCommand = "";
                    if (args.length != 0) {
                        subSubCommand = args[0];
                    }

                    if (subCommand.commandExist(subSubCommand)) {
                        String[] newArgs;
                        if (args.length == 0) {
                            newArgs = args;
                        } else {
                            newArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                        }
                        ((SubCommand) entry).execute(subSubCommand, sender, newArgs);
                    }
                }
            } else {
                if (sender instanceof ConsoleCommandSender) {
                    commandHandler.getServerCaller().getPlayerCaller().sendMessage(sender.toString(), "{{DARK_GREEN}}========= {{WHITE}}Help {{DARK_GREEN}}========");
                    for (Map.Entry<String, Command> iteratorEntry : commandList.entrySet()) {
                        Command commandEntry = iteratorEntry.getValue();
                        if (commandEntry instanceof CommandExecutor) {
                            CommandExecutor cmd = ((CommandExecutor) iteratorEntry.getValue());
                            commandHandler.getServerCaller().getPlayerCaller().sendMessage(sender.toString(), cmd.help());
                        } else {
                            SubCommand subCmd = (SubCommand) commandEntry;
                            String subCommandResult = "";
                            while (subCmd.parent != null) {
                                subCommandResult = subCmd.parent.name + "" + subCommandResult;
                            }
                            subCommandResult = "/" + subCommandResult + " " + subCmd.name;
                            commandHandler.getServerCaller().getPlayerCaller().sendMessage(sender.toString(), subCommandResult);
                        }
                    }
                } else if (sender instanceof PlayerCommandSender) {
                    commandHandler.getServerCaller().getPlayerCaller().sendMessage(((PlayerCommandSender) sender).getUuid(), "{{DARK_GREEN}}========= {{WHITE}}Help {{DARK_GREEN}}========");
                    for (Map.Entry<String, Command> iteratorEntry : commandList.entrySet()) {
                        Command commandEntry = iteratorEntry.getValue();
                        if (commandEntry instanceof CommandExecutor) {
                            String perm = ((CommandExecutor) commandEntry).getPermissionNode();
                            if (commandHandler.getServerCaller().getPlayerCaller().checkPermission(((PlayerCommandSender) sender).getUuid(), perm)) {
                                commandHandler.getServerCaller().getPlayerCaller().sendMessage(((PlayerCommandSender) sender).getUuid(), ((CommandExecutor) commandEntry).help());
                            }
                        } else {
                            SubCommand subCmd = (SubCommand) commandEntry;
                            String subCommandResult = "";
                            while (subCmd.parent != null) {
                                subCommandResult = subCmd.parent.name + "" + subCommandResult;
                            }
                            subCommandResult = "/" + subCommandResult + " " + subCmd.name;
                            commandHandler.getServerCaller().getPlayerCaller().sendMessage(((PlayerCommandSender) sender).getUuid(), subCommandResult);
                        }

                    }

                }
            }
        } else {
            commandHandler.getServerCaller().getPlayerCaller().sendMessage(sender.toString(), commandHandler.getWrongLevelMsg());
        }
    }

    public Set<String> getSubCommandKeys() {
        return Collections.unmodifiableSet(commandList.keySet());
    }

    public String getSubCommandList() {
        return Arrays.toString(commandList.keySet().toArray());
    }
}
