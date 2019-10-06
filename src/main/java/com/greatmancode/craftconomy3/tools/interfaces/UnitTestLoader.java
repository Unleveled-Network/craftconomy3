/**
 * This file is part of GreatmancodeTools.
 *
 * Copyright (c) 2013-2016, Greatman <http://github.com/greatman/>
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
package com.greatmancode.craftconomy3.tools.interfaces;

import com.greatmancode.craftconomy3.tools.ServerType;
import com.greatmancode.craftconomy3.tools.caller.unittest.UnitTestServerCaller;
import com.greatmancode.craftconomy3.tools.commands.interfaces.CommandReceiver;
import com.greatmancode.craftconomy3.tools.events.EventManager;

public class UnitTestLoader implements Loader {
    private EventManager eventManager;

    public UnitTestLoader() {
        this.eventManager = new EventManager(new UnitTestServerCaller(this));
    }

    @Override
    public void onEnable() {
        // Nothing
        // We don't need to load any config
    }

    @Override
    public void onDisable() {
        // Nothing
    }

    @Override
    public ServerType getServerType() {
        return ServerType.UNIT_TEST;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public java.util.logging.Logger getLogger() {
        return java.util.logging.Logger.getLogger("GreatmanCodeTools");
    }

    @Override
    public Common getCommon() {
        return null;
    }

    @Override
    public CommandReceiver getCommandReceiver() {
        return null;
    }

    @Override
    public void setCommandReceiver(CommandReceiver receiver) {

    }
}
