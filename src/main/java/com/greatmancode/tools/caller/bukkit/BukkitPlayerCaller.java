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
package com.greatmancode.tools.caller.bukkit;

import com.greatmancode.tools.interfaces.BukkitLoader;
import com.greatmancode.tools.interfaces.caller.PlayerCaller;
import com.greatmancode.tools.interfaces.caller.ServerCaller;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitPlayerCaller extends PlayerCaller {
    public BukkitPlayerCaller(ServerCaller caller) {
        super(caller);
    }


    private Player getBukkitPlayer(UUID uuid){
        return ((BukkitLoader) getCaller().getLoader()).getServer().getPlayer(uuid);

    }

    private OfflinePlayer getOfflinePlayer(UUID uuid){
        return ((BukkitLoader) getCaller().getLoader()).getServer().getOfflinePlayer(uuid);

    }
    
    @Deprecated
    @Override
    public boolean checkPermission(String playerName, String perm) {
        boolean result;
        Player p = ((BukkitLoader) getCaller().getLoader()).getServer().getPlayerExact(playerName);
        if (p != null) {
            result = p.isOp() || p.hasPermission(perm);
        } else {
            // It's the console
            result = true;
        }
        return result;
    }

    @Override
    public boolean checkPermission(UUID uuid, String perm) {
        Player p = getBukkitPlayer(uuid);
        if (p != null) {
            return (p.isOp() || p.hasPermission(perm));
        } else {
//            OfflinePlayer off = getOfflinePlayer(uuid);
//            if(off != null){
//                return false;
//            }
            return true;
        }
    }
    
    @Deprecated
    @Override
    public void sendMessage(String playerName, String message) {
        Player p = ((BukkitLoader) getCaller().getLoader()).getServer().getPlayerExact(playerName);
        if (p != null) {
            p.sendMessage(getCaller().addColor(getCaller().getCommandPrefix() + message));
        } else {
            ((BukkitLoader) getCaller().getLoader()).getServer().getConsoleSender().sendMessage(getCaller().addColor(getCaller().getCommandPrefix() + message));
        }
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        Player p = getBukkitPlayer(uuid);
        if (p != null) {
            p.sendMessage(getCaller().addColor(getCaller().getCommandPrefix() + message));
        } else {
            ((BukkitLoader) getCaller().getLoader()).getServer().getConsoleSender().sendMessage(getCaller().addColor(getCaller().getCommandPrefix() + message));
        }
    }
    
    @Deprecated
    @Override
    public String getPlayerWorld(String playerName) {
        Player p = ((BukkitLoader) getCaller().getLoader()).getServer().getPlayerExact(playerName);
        return p != null ? p.getWorld().getName() : "";
    }

    @Override
    public String getPlayerWorld(UUID uuid) {
        Player p = ((BukkitLoader) getCaller().getLoader()).getServer().getPlayer(uuid);
        return (p != null) ? p.getWorld().getName() : "";
    }

    @Deprecated
    @Override
    public boolean isOnline(String playerName) {
        return ((BukkitLoader) getCaller().getLoader()).getServer().getPlayerExact(playerName) != null;
    }
    
    @Override
    public boolean isOnline(UUID uuid) {
        return ((BukkitLoader) getCaller().getLoader()).getServer().getPlayer(uuid) != null;
    }
    
    @Override
    @Deprecated
    public List<String> getOnlinePlayers() {
        List<String> list = new ArrayList<String>();
        for (Player p : ((BukkitLoader) getCaller().getLoader()).getServer().getOnlinePlayers()) {
            list.add(p.getName());
        }
        return list;
    }
    
    @Override
    public List<UUID> getUUIDsOnlinePlayers() {
        List<UUID> result = new ArrayList<>();
        for (Player player:  ((BukkitLoader) getCaller().getLoader()).getServer().getOnlinePlayers()){
            result.add(player.getUniqueId());
        }
        return result;
    }

    /**
     *
     * @param playerName The player name to check
     * @return boolean true if Op
     * @deprecated Use {@code isOp(UUID) }
     */
    
    @Deprecated
    @Override
    public boolean isOp(String playerName) {
        return ((BukkitLoader) getCaller().getLoader()).getServer().getOfflinePlayer(playerName).isOp();
    }

    @Override
    public boolean isOP(UUID uuid) {
        return ((BukkitLoader) getCaller().getLoader()).getServer().getOfflinePlayer(uuid).isOp();
    }

    @Override
    @Deprecated
    public UUID getUUID(String playerName) {
        OfflinePlayer offline = Bukkit.getOfflinePlayer(playerName);
        UUID uuid = offline.getUniqueId();
        Player player = getBukkitPlayer(uuid);
        if (player != null){
            return player.getUniqueId();
        }
        getCaller().getLogger().warning("Returning offline Player UUID for : " + playerName);
        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }
    
    @Override
    public String getPlayerName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
    
    @Override
    public com.greatmancode.tools.entities.Player getPlayer(UUID uuid) {
        Player player = ((BukkitLoader) getCaller().getLoader()).getServer().getPlayer(uuid);
        if (player == null)return null;
        return new com.greatmancode.tools.entities.Player(player.getName(),
                player.getDisplayName(),player.getWorld().getName(),
                player.getUniqueId());
    }
    
    @Override
    @Deprecated
    public com.greatmancode.tools.entities.Player getOnlinePlayer(String name) {
        Player player = ((BukkitLoader) getCaller().getLoader()).getServer().getPlayerExact(name);
        if( player != null && player.isOnline()){
            return new com.greatmancode.tools.entities.Player(player.getName(),
                    player.getDisplayName(),player.getWorld().getName(),
                    player.getUniqueId());
        }
        return null;
    }
    
    @Override
    public com.greatmancode.tools.entities.Player getOnlinePlayer(UUID uuid) {
        Player player = ((BukkitLoader) getCaller().getLoader()).getServer().getPlayer(uuid);
        if (player == null)return null;
        if(player.isOnline()) {
            return new com.greatmancode.tools.entities.Player(player.getName(),
                    player.getDisplayName(), player.getWorld().getName(),
                    player.getUniqueId());
        }
        return null;
    }
}
