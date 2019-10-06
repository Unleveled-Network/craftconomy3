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
package com.greatmancode.craftconomy3.tools.configuration;

import com.greatmancode.craftconomy3.tools.configuration.bukkit.BukkitConfig;
import com.greatmancode.craftconomy3.tools.interfaces.caller.ServerCaller;

import java.io.File;

/**
 * Configuration Loader. Load the configuration with the Server configuration manager.
 *
 * @author greatman
 */
public class ConfigurationManager {
    private ServerCaller serverCaller;

    public ConfigurationManager(ServerCaller serverCaller) {
        this.serverCaller = serverCaller;
    }

    public Config loadFile(File folder, String fileName) {
        return new BukkitConfig(folder, fileName, serverCaller);
    }
}
