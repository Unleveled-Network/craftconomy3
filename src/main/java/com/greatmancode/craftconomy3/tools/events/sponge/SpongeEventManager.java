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
package com.greatmancode.craftconomy3.tools.events.sponge;

import com.greatmancode.craftconomy3.tools.events.interfaces.ServerEventManager;
import com.greatmancode.craftconomy3.tools.events.playerevent.PlayerJoinEvent;
import com.greatmancode.craftconomy3.tools.interfaces.SpongeLoader;
import com.greatmancode.craftconomy3.tools.interfaces.caller.ServerCaller;

import java.util.HashMap;
import java.util.Map;

public class SpongeEventManager implements ServerEventManager {
    private Map<String, Object> map = new HashMap<>();

    public SpongeEventManager() {
        map.put(PlayerJoinEvent.class.getName(), new com.greatmancode.craftconomy3.tools.events.sponge.PlayerJoinEventListener());
    }

    @Override
    public void eventRegistered(String event, ServerCaller serverCaller) {
        if (map.containsKey(event)) {
            ((SpongeLoader)serverCaller.getLoader()).getGame().getEventManager().registerListeners(serverCaller.getLoader(), map.get(event));
        }
    }
}
