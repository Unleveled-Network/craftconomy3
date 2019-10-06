/**
 * This file is part of GreatmancodeTools.
 *
 * Copyright (c) 2013-2016, Greatman <http://github.com/greatman/>
 *
 * GreatmancodeTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GreatmancodeTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GreatmancodeTools.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.tools.interfaces.caller;

import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.entities.Player;

import java.util.List;
import java.util.UUID;

public abstract class PlayerCaller {
    protected final ServerCaller caller;

    public PlayerCaller(ServerCaller caller) {
        this.caller = caller;
    }

    public ServerCaller getCaller() {
        return caller;
    }

    /**
     * Check the permissions of a player
     *
     * @param playerName The player name to check
     * @param perm       The permission node to check
     * @return True if the player have the permission. Else false (Always true for the Console)
     */
    @Deprecated
    public abstract boolean checkPermission(String playerName, String perm);

    /**
     * Check the permissions of a player
     *
     * @param uuid The player UUID name to check
     * @param perm       The permission node to check
     * @return True if the player have the permission. Else false (Always true for the Console)
     */
    public abstract boolean checkPermission(UUID uuid, String perm);
    
    /**
     * Sends a message to a player
     * @param playerName The player name to send the message
     * @param message    The message to send
     * @param commandName the command that started the mes
     */
    public abstract void sendMessage(String playerName, String message, String commandName);

    /**
     * Sends a message to a player
     *
     * @param playerName The player name to send the message
     * @param message    The message to send
     */
    @Deprecated
    public abstract void sendMessage(String playerName, String message);

    /**
     * Sends a message to a sender
     * @param sender the sender
     * @param message the message
     * @param command the initial command
     */
    public abstract void sendMessage(CommandSender sender, String message, String command);

    /**
     * Sends a message to a player
     * @param commandName the command that started the mes
     * @param uuid The player name to send the message
     * @param message    The message to send
     */
    public abstract void sendMessage(UUID uuid, String message, String commandName);
    
    /**
     * Sends a message to a player
     *
     * @param uuid The player uuid to send the message
     * @param message    The message to send
     */
    
    public abstract void sendMessage(UUID uuid, String message);
    
    /**
     * Retrieve the world name that a player is currently in
     *
     * @param playerName The player name to retrieve the world
     * @return The world name the player is currently in. Returns "" when the player is offline
     */
    @Deprecated
    public abstract String getPlayerWorld(String playerName);
    
    /**
     * Retrieve the world name that a player is currently in
     *
     * @param uuid The player uuid to retrieve the world
     * @return The world name the player is currently in. Returns "" when the player is offline
     */
    public abstract String getPlayerWorld(UUID uuid);


    /**
     * Checks if a player is online
     *
     * @param playerName The player name
     * @return True if the player is online. Else false.
     */
    @Deprecated
    public abstract boolean isOnline(String playerName);
    
    
    /**
     * Checks if a player is online
     *
     * @param uuid The player name
     * @return True if the player is online. Else false.
     */
    public abstract boolean isOnline(UUID uuid);

    /**
     * Retrieve a list of online players
     *
     * @return A list of all players online.
     */
    @Deprecated
    public abstract List<String> getOnlinePlayers();
    
    /**
     * Retrieve a list of online players
     *
     * @return A list of all players online.
     */
    public abstract List<UUID> getUUIDsOnlinePlayers();

    /**
     * Check if the user is a Operator.
     *
     * @param playerName The player name to check
     * @return True if the player is a OP else false.
     */
    @Deprecated
    public abstract boolean isOp(String playerName);
    /**
     * Check if the user is a Operator.
     *
     * @param uuid The player uuid to check
     * @return True if the player is a OP else false.
     */

    public abstract boolean isOP(UUID uuid);
    /**
     * Retrieve the UUID of a player.
     * @param playerName The player name
     * @return The UUID of the player
     */

    public abstract UUID getUUID(String playerName);
    
    /**
     * Return the player name for a specific uuid
     *
     * @param uuid UUID of the player
     * @return The players name
     */
    public abstract String getPlayerName(UUID uuid);
    
    /**
     *
     * @param uuid the uuid to check
     * @return @{code Player} Player object
     */
    public abstract Player getPlayer(UUID uuid);
    
    /**
     *
     * @param name name of online player to check
     * @return The player
     */
    public abstract Player getOnlinePlayer(String name);
    
    /**
     * @param uuid uuid of online player to check
     * @return The player
     */
    public abstract Player getOnlinePlayer(UUID uuid);
    
}
