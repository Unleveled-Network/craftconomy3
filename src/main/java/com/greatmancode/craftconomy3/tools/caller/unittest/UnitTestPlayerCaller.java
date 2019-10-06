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
package com.greatmancode.craftconomy3.tools.caller.unittest;

import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.entities.Player;
import com.greatmancode.craftconomy3.tools.interfaces.caller.PlayerCaller;
import com.greatmancode.craftconomy3.tools.interfaces.caller.ServerCaller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UnitTestPlayerCaller extends PlayerCaller {
    private UUID playeruuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private UnitTestCommandSender sender = new UnitTestCommandSender("UnitTestPlayer", playeruuid);
    private HashMap<UUID, ArrayList<String>> permissions = new HashMap<>();

    public UnitTestPlayerCaller(ServerCaller caller) {
        super(caller);
    }

    @Override
    public boolean checkPermission(String playerName, String perm) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean checkPermission(UUID uuid, String perm) {
        return this.permissions.containsKey(uuid) && this.permissions.get(uuid).contains(perm);

    }

    /**
     * Adds a permission to a player.
     * This is only possible in unit tests.
     *
     * @param uuid The uuid of the player
     * @param perm The permission string
     */
    public void addPermission(UUID uuid, String perm) {
        ArrayList<String> playersPermissions = new ArrayList<>();

        if (this.permissions.containsKey(uuid)) {
            playersPermissions = this.permissions.get(uuid);
        }

        playersPermissions.add(perm);
        this.permissions.put(uuid, playersPermissions);
    }

    @Override
    public void sendMessage(String playerName, String message, String commandName) {
        caller.getLogger().info(playerName + ":" + message + " from " + commandName);

    }

    @Override
    public void sendMessage(String playerName, String message) {
        caller.getLogger().info(playerName + ":" + message);
    }

    @Override
    public void sendMessage(CommandSender sender, String message, String command) {
        caller.getLogger().info(sender.toString() + ":" + message + " from " + command);
    }

    @Override
    public void sendMessage(UUID uuid, String message, String commandName) {
        caller.getLogger().info(uuid.toString() + ":" + message + " from " + commandName);
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        caller.getLogger().info(uuid.toString() + ":" + message);
    }

    @Override
    public String getPlayerWorld(String playerName) {
        return "UnitTestWorld";
    }

    @Override
    public String getPlayerWorld(UUID uuid) {
        return "UnitTestWorld";
    }

    @Override
    public boolean isOnline(String playerName) {
        return playerName.equals("console");
    }

    @Override
    public boolean isOnline(UUID uuid) {
        return uuid.equals(playeruuid);
    }

    @Override
    @Deprecated
    public boolean isOp(String playerName) {
        return "UnitTestPlayer".equals(playerName);
    }

    @Override
    public boolean isOP(UUID uuid) {
        return uuid.equals(playeruuid);
    }

    @Override
    public UUID getUUID(String playerName) {
        return playeruuid;
    }

    @Override
    public String getPlayerName(UUID uuid) {
        return "UnitTestPlayer";
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return new Player("UnitTestPlayer", "UnitTestPlayer", "UnitTestWorld", playeruuid, sender);
    }

    @Override
    public Player getOnlinePlayer(String name) {
        return new Player("UnitTestPlayer", "UnitTestPlayer", "UnitTestWorld", playeruuid, sender);
    }

    @Override
    public Player getOnlinePlayer(UUID uuid) {
        return new Player("UnitTestPlayer", "UnitTestPlayer", "UnitTestWorld", playeruuid, sender);
    }

    @Override
    public List<String> getOnlinePlayers() {
        List<String> list = new ArrayList<>();
        list.add("UnitTestPlayer");
        return list;
    }

    @Override
    public List<UUID> getUUIDsOnlinePlayers() {
        List<UUID> list = new ArrayList<>();

        list.add(playeruuid);
        return list;
    }
}
