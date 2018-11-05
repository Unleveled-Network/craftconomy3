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
package com.greatmancode.tools.caller.sponge;

import com.greatmancode.tools.interfaces.SpongeLoader;
import com.greatmancode.tools.interfaces.caller.PlayerCaller;
import com.greatmancode.tools.interfaces.caller.ServerCaller;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.util.Identifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpongePlayerCaller extends PlayerCaller {

    private SpongeLoader loader;

    public SpongePlayerCaller(ServerCaller caller) {
        super(caller);

        loader = ((SpongeLoader) caller.getLoader());
    }

    @Override
    @Deprecated
    public boolean checkPermission(String playerName, String perm) {
        if (playerName.equals("console")) {
            return true;
        }
        UUID uuid = getUUID(playerName);
        return checkPermission(uuid, perm);
    }

    @Override
    public boolean checkPermission(UUID uuid, String perm) {
        return loader.getGame().getServiceManager().provide(PermissionService.class).get().getUserSubjects().get(uuid.toString()).hasPermission(perm);
    }

    @Override
    @Deprecated
    public void sendMessage(String playerName, String message) {
        if (playerName.equals("console")) {
            caller.getLogger().info(message);
            return;
        }
        loader.getGame().getServer().getPlayer(playerName).get().sendMessage(((SpongeServerCaller)getCaller()).addColorSponge(message));
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        loader.getGame().getServer().getPlayer(uuid).get().sendMessage(((SpongeServerCaller)getCaller()).addColorSponge(message));
    }

    @Override
    @Deprecated
    public String getPlayerWorld(String playerName) {
        return loader.getGame().getServer().getPlayer(playerName).get().getWorld().getName();
    }

    @Override
    public String getPlayerWorld(UUID uuid) {
        return loader.getGame().getServer().getPlayer(uuid).get().getWorld().getName();
    }

    @Override
    @Deprecated
    public boolean isOnline(String playerName) {
        return loader.getGame().getServer().getPlayer(playerName).isPresent();
    }

    @Override
    public boolean isOnline(UUID uuid) {
        return false;
    }

    @Override
    @Deprecated
    public List<String> getOnlinePlayers() {
        List<String> playerList = new ArrayList<>();
        for (Player p : loader.getGame().getServer().getOnlinePlayers()) {
            playerList.add(p.getName());
        }
        return playerList;
    }

    @Override
    public List<UUID> getUUIDsOnlinePlayers() {
        return null;
    }

    @Override
    @Deprecated
    public boolean isOp(String playerName) {
        return false;
    }

    @Override
    public boolean isOP(UUID uuid) {
        return false;
    }

    @Override
    public UUID getUUID(String playerName) {
        Optional<Player> result = loader.getGame().getServer().getPlayer(playerName);
        return result.map(Identifiable::getUniqueId).orElse(null);
    }

    @Override
    public String getPlayerName(UUID uuid) {
        return null;
    }

    @Override
    public com.greatmancode.tools.entities.Player getPlayer(UUID uuid) {
        Optional<Player> result = loader.getGame().getServer().getPlayer(uuid);
        return result.map(player -> new com.greatmancode.tools.entities.Player(player.getName(), player.getDisplayNameData().displayName().toString(), player.getWorld().getName(), player.getUniqueId())).orElse(null);
    }
    
    @Override
    public com.greatmancode.tools.entities.Player getOnlinePlayer(String name) {
        Optional<Player> result = loader.getGame().getServer().getPlayer(name);
        if (result.isPresent() && result.get().isOnline()) {
            return result.map(player ->
                    new com.greatmancode.tools.entities.Player(player.getName(), player.getDisplayNameData().displayName().toString(), player.getWorld().getName(), player.getUniqueId())).orElse(null);
        }
        return null;
    }
    
    @Override
    public com.greatmancode.tools.entities.Player getOnlinePlayer(UUID uuid) {
        Optional<Player> result = loader.getGame().getServer().getPlayer(uuid);
        if (result.isPresent() && result.get().isOnline()) {
            return result.map(player ->
                    new com.greatmancode.tools.entities.Player(player.getName(), player.getDisplayNameData().displayName().toString(), player.getWorld().getName(), player.getUniqueId())).orElse(null);
        }
        return null;
    }
}
