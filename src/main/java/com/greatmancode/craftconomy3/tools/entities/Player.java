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
package com.greatmancode.craftconomy3.tools.entities;

import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.commands.PlayerCommandSender;
import com.greatmancode.craftconomy3.tools.interfaces.caller.PlayerCaller;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class Player extends PlayerCommandSender<CommandSender> {

    private final String displayName;
    private final String worldName;

    public Player(String name, String displayName, String worldName, UUID uuid) {
        super(name, uuid, null);
        this.displayName = displayName;
        this.worldName = worldName;
    }

    public Player(String name, String displayName, String worldName, UUID uuid, CommandSender sender) {
        super(name, uuid, sender);
        this.displayName = displayName;
        this.worldName = worldName;
    }

    public static Player getPlayer(PlayerCaller caller, UUID uuid) {
        return caller.getPlayer(uuid);
    }

}
