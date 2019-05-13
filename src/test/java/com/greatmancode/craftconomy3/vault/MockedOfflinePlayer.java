package com.greatmancode.craftconomy3.vault;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class MockedOfflinePlayer implements OfflinePlayer {

    private UUID uuid;
    private String name;
    private boolean isWhitelisted;
    private boolean isBanned;
    private boolean isOp;

    public MockedOfflinePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.isWhitelisted = true;
        this.isBanned = false;
        this.isOp = false;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public boolean isBanned() {
        return this.isBanned;
    }

    @Override
    public boolean isWhitelisted() {
        return this.isWhitelisted;
    }

    @Override
    public void setWhitelisted(boolean value) {
        this.isWhitelisted = value;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public long getFirstPlayed() {
        return 0;
    }

    @Override
    public long getLastPlayed() {
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        return false;
    }

    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }

    @Override
    public boolean isOp() {
        return this.isOp;
    }

    @Override
    public void setOp(boolean value) {
        this.isOp = value;
    }
}
