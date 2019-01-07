package com.greatmancode.tools.caller.unittest;

import com.greatmancode.tools.commands.CommandSender;

import java.util.UUID;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 14/12/2018.
 */
public class UnitTestCommandSender implements CommandSender<UnitSender> {
    String name;
    UUID uuid;
    public UnitTestCommandSender(String name, UUID uuid) {
        this.name =name;
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public UnitSender getServerSender() {
        return new UnitSender();
    }
}
