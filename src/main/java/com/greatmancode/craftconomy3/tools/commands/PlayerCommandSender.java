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

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Created for the AddstarMC Program.
 * <p>
 * Created by Narimm on 19/02/2018.
 */
public class PlayerCommandSender<T> implements CommandSender<T> {
    @NonNull
    private final String name;
    @NonNull
    private final UUID uuid;
    private T sender;

    public PlayerCommandSender(@NonNull final String name, @NonNull final UUID uuid, final T sender) {
        if (name == null) {
            throw new java.lang.NullPointerException("name is marked non-null but is null");
        }
        if (uuid == null) {
            throw new java.lang.NullPointerException("uuid is marked non-null but is null");
        }
        this.name = name;
        this.uuid = uuid;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public T getServerSender() {
        return sender;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public UUID getUuid() {
        return this.uuid;
    }

    public T getSender() {
        return this.sender;
    }

    public void setSender(final T sender) {
        this.sender = sender;
    }
}
