/**
 * This file is part of Craftconomy3.
 * <p>
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018, Pavog <http://github.com/pavog/>
 * <p>
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.tools.configuration.sponge;

import com.google.common.reflect.TypeToken;
import com.greatmancode.tools.configuration.Config;
import com.greatmancode.tools.interfaces.caller.ServerCaller;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SpongeConfig extends Config {

    private CommentedConfigurationNode file;
    private HoconConfigurationLoader loader;

    public SpongeConfig(InputStreamReader re, ServerCaller serverCaller) {
        super(re, serverCaller);
        //No can't do.
    }

    public SpongeConfig(File folder, String fileName, ServerCaller serverCaller) {
        super(folder, fileName, serverCaller);
        try {
            loader = HoconConfigurationLoader.builder().setFile(new File(folder, fileName)).build();
            file = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getInt(String path, int def) {
        return file.getNode(path).getInt(def);
    }

    @Override
    public long getLong(String path, long def) {
        return file.getNode(path).getLong(def);
    }

    @Override
    public double getDouble(String path, double def) {
        return file.getNode(path).getDouble(def);
    }

    @Override
    public String getString(String path, String def) {
        return file.getNode(path).getString(def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return file.getNode(path).getBoolean(def);
    }

    @Override
    public Map<String, String> getStringMap(String path, Map<String, String> def) {
        Map<String, String> map = new HashMap<>();
        Set<? extends Map.Entry<Object, ? extends CommentedConfigurationNode>> childrens = path.equals("") ? file.getChildrenMap().entrySet() : file.getNode(path).getChildrenMap().entrySet();
        for (Map.Entry<Object, ? extends CommentedConfigurationNode> entry : childrens) {
            map.put(entry.getKey().toString(), entry.getValue().getString());
        }

        return map.isEmpty() ? def : map;
    }

    /**
     * Returns an empty list if the path does not exist in the config.
     */
    @Override
    public List<String> getStringList(String path, List<String> def) {
        List<String> list = new ArrayList<>();

        // Return empty list if path does not exist
        if (file.getNode(path).isVirtual())
            return list;

        try {
            list = file.getNode(path).getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            return list;
        }

        return list;
    }

    @Override
    public void setValue(String path, Object value) {
        file.getNode(path).setValue(value);
        try {
            loader.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean has(String path) {
        return !file.getNode(path).isVirtual();
    }

}
