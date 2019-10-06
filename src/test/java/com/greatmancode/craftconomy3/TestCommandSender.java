package com.greatmancode.craftconomy3;


import com.greatmancode.craftconomy3.tools.commands.CommandSender;

import java.util.UUID;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 14/12/2018.
 */
public class TestCommandSender implements CommandSender {

    private String name;
    private UUID uuid;

    public TestCommandSender(UUID uuid, String name) {
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object getServerSender() {
        return null;
    }
}
