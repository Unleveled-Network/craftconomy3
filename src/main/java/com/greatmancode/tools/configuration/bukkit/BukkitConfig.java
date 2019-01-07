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

package com.greatmancode.tools.configuration.bukkit;

import com.greatmancode.tools.configuration.Config;
import com.greatmancode.tools.interfaces.caller.ServerCaller;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This class handles YAML files with the Bukkit imports.
 */
public class BukkitConfig extends Config {
    private final YamlConfiguration configFile;

    public BukkitConfig(InputStreamReader re, ServerCaller serverCaller) {
        super(re, serverCaller);
        configFile = YamlConfiguration.loadConfiguration(re);
    }

    public BukkitConfig(File folder, String fileName, ServerCaller serverCaller) {
        super(folder, fileName, serverCaller);
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public int getInt(String path,int def) {
        return configFile.getInt(path,def);
    }

    @Override
    public long getLong(String path,long def) {
        return configFile.getLong(path,def);
    }

    @Override
    public double getDouble(String path, double def) {
        return configFile.getDouble(path,def);
    }

    @Override
    public String getString(String path, String def) {
        return configFile.getString(path,def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return configFile.getBoolean(path,def);
    }

    @Override
    public void setValue(String path, Object value) {
        configFile.set(path, value);
        try {
            configFile.save(file);
        } catch (IOException e) {
            serverCaller.getLogger().severe("Error while saving + " + file.getName() + ". Error: " + e.getMessage());
        }
    }

    @Override
    public boolean has(String path) {
        return configFile.contains(path);
    }

    @Override
    public Map<String, String> getStringMap(String path,Map<String, String> def) {
        Map<String, String> values = new HashMap<>();
        ConfigurationSection configurationSection = configFile.getConfigurationSection(path);
        if (configurationSection != null) {
            for (Map.Entry<String, Object> entry : configurationSection.getValues(false).entrySet()) {
                values.put(entry.getKey(), (String) entry.getValue());
            }
            return values;
        }else{
            return def;
        }
    }

    @Override
    public List<String> getStringList(String path, List<String> def) {
        List<?> list = configFile.getList(path);
        if (list == null) {
            return def;
        }
        List<String> result = new ArrayList<>();
    
        for (Object object : list) {
            if ((object instanceof String) || (isPrimitiveWrapper(object))) {
                result.add(String.valueOf(object));
            }
        }
        return result;
    }
    
    protected boolean isPrimitiveWrapper(Object input) {
        return input instanceof Integer || input instanceof Boolean || input instanceof Character || input instanceof Byte || input instanceof Short || input instanceof Double || input instanceof Long || input instanceof Float;
    }
}
