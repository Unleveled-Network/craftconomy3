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
package com.greatmancode.craftconomy3.tools.events;

import com.greatmancode.craftconomy3.tools.caller.bukkit.BukkitPlayerCaller;
import com.greatmancode.craftconomy3.tools.caller.bukkit.BukkitServerCaller;
import com.greatmancode.craftconomy3.tools.caller.unittest.UnitTestServerCaller;
import com.greatmancode.craftconomy3.tools.events.bukkit.BukkitEventManager;
import com.greatmancode.craftconomy3.tools.events.interfaces.EventHandler;
import com.greatmancode.craftconomy3.tools.events.interfaces.Listener;
import com.greatmancode.craftconomy3.tools.events.interfaces.ServerEventManager;
import com.greatmancode.craftconomy3.tools.events.unittest.UnitTestEventManager;
import com.greatmancode.craftconomy3.tools.interfaces.Common;
import com.greatmancode.craftconomy3.tools.interfaces.caller.ServerCaller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EventManager {
    private Map<String, ListenerRegistration> eventList = new HashMap<>();
    private static EventManager instance;
    private ServerEventManager eventManager;
    private ServerCaller serverCaller;

    public EventManager(ServerCaller serverCaller) {
        instance = this;
        this.serverCaller = serverCaller;

        if (serverCaller instanceof BukkitServerCaller) {
            eventManager = new BukkitEventManager((BukkitPlayerCaller) serverCaller.getPlayerCaller());
        } else if (serverCaller instanceof UnitTestServerCaller) {
            eventManager = new UnitTestEventManager();
        }
    }

    public static EventManager getInstance() {
        return instance;
    }

    public Event callEvent(Event event) {
        if (eventList.containsKey(event.getClass().getName())) {
            eventList.get(event.getClass().getName()).callEvent(event);
        }
        return event;
    }

    protected void registerEvents(Listener listener) {
        if (listener != null) {
            internalRegisterEvents(listener);
        }
    }

    //TODO: Do something with the plugin var.
    public void registerEvents(Common commonInterface, Listener listener) {
        if (commonInterface != null && listener != null) {
            internalRegisterEvents(listener);
        }
    }

    private void internalRegisterEvents(Listener listener) {
        if (listener != null) {
            Method[] methods = listener.getClass().getMethods();
            for (Method method : methods) {
                if (method.getAnnotation(EventHandler.class) != null) {
                    Class<?>[] parameters = method.getParameterTypes();
                    if (parameters.length == 1) {
                        for (Class<?> parameter : parameters) {
                            if (Event.class.isAssignableFrom(parameter)) {
                                eventManager.eventRegistered(parameter.getName(), serverCaller);
                                if (eventList.containsKey(parameter.getName())) {
                                    eventList.get(parameter.getName()).addListener(listener, method);
                                } else {
                                    ListenerRegistration registration = new ListenerRegistration();
                                    registration.addListener(listener, method);
                                    eventList.put(parameter.getName(), registration);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
