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
package com.greatmancode.tools.caller.unittest;

import com.greatmancode.tools.entities.Player;
import com.greatmancode.tools.interfaces.caller.PlayerCaller;
import com.greatmancode.tools.interfaces.caller.ServerCaller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnitTestPlayerCaller extends PlayerCaller {
    public UnitTestPlayerCaller(ServerCaller caller) {
        super(caller);
    }

    @Override
    public ServerCaller getCaller() {
        return super.getCaller();
    }

    @Override
    @Deprecated
    public boolean checkPermission(String playerName, String perm) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean checkPermission(UUID uuid, String perm) {
        return false;
    }

    @Override
    @Deprecated
    public void sendMessage(String playerName, String message) {
        caller.getLogger().info(playerName + ":" + message);
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        caller.getLogger().info(uuid.toString() + ":" + message);
    }

    @Override
    @Deprecated
    public String getPlayerWorld(String playerName) {
        return "UnitTestWorld";
    }

    @Override
    public String getPlayerWorld(UUID uuid) {
        return "UnitTestWorld";
    }

    @Override
    @Deprecated
    public boolean isOnline(String playerName) {
        return playerName.equals("console");
    }

    @Override
    public boolean isOnline(UUID uuid) {
        return uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Override
    @Deprecated
    public boolean isOp(String playerName) {
        return playerName.equals("UnitTestPlayer");
    }

    @Override
    public boolean isOP(UUID uuid) {
        return uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Override
    public UUID getUUID(String playerName) {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    @Override
    public String getPlayerName(UUID uuid) {
        return "UnitTestPlayer";
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return new Player("UnitTestPlayer", "UnitTestPlayer", "UnitTestWorld", UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Override
    public Player getOnlinePlayer(String name) {
        return new Player("UnitTestPlayer", "UnitTestPlayer", "UnitTestWorld", UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Override
    public Player getOnlinePlayer(UUID uuid) {
        return new Player("UnitTestPlayer", "UnitTestPlayer", "UnitTestWorld", UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Override
    @Deprecated
    public List<String> getOnlinePlayers() {
        List<String> list = new ArrayList<>();
        list.add("UnitTestPlayer");
        return list;
    }

    @Override
    public List<UUID> getUUIDsOnlinePlayers() {
        List<UUID> list = new ArrayList<>();

        list.add(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        return list;
    }
}
