/**
 * This file is part of GreatmancodeTools.
 *
 * Copyright (c) 2013-2016, Narimm <http://github.com/narimm/>
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

package com.greatmancode.tools.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

/**
 * Created for the AddstarMC Program.
 * Created by Narimm on 19/02/2018.
 */

@Data
@AllArgsConstructor
public class PlayerCommandSender<T> implements CommandSender<T> {
    
    @NonNull
    private final String name;
    @NonNull
    private final UUID uuid;

    private T sender;
    @Override
    public String toString() {
        return name;
    }

    @Override
    public T getServerSender() {
        return sender;
    }
}
