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
package com.greatmancode.craftconomy3.commands;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.TestCommandSender;
import com.greatmancode.craftconomy3.TestInitializator;
import com.greatmancode.craftconomy3.commands.group.GroupAddWorldCommand;
import com.greatmancode.craftconomy3.commands.group.GroupCreateCommand;
import com.greatmancode.craftconomy3.commands.group.GroupDelWorldCommand;
import com.greatmancode.craftconomy3.groups.WorldGroupsManager;
import com.greatmancode.tools.caller.unittest.UnitTestServerCaller;
import com.greatmancode.tools.commands.PlayerCommandSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class TestGroupCommands {

    private static PlayerCommandSender TEST_USER;

    @Before
    public void setUp() {
        new TestInitializator();
        TEST_USER = createTestUser("testuser50");
    }

    @After
    public void close() {
        Common.getInstance().onDisable();
    }

    @Test
    public void testAddWorldCommand() {
        GroupAddWorldCommand command = new GroupAddWorldCommand("add");

        // Test with default world and worldgroup
        command.execute(TEST_USER, new String[]{WorldGroupsManager.DEFAULT_GROUP_NAME, UnitTestServerCaller.worldName});

        // Test without creating the worldgroup before assigning a world to it
        command.execute(TEST_USER, new String[]{"worldgroup1", UnitTestServerCaller.worldName});

        // Test with other worldgroup
        // Create new world group
        final String newWorldGroupName = "worldgroup2";
        Common.getInstance().getWorldGroupManager().addWorldGroup(newWorldGroupName);

        command.execute(TEST_USER, new String[]{newWorldGroupName, UnitTestServerCaller.worldName2});
        assertEquals(newWorldGroupName, Common.getInstance().getWorldGroupManager().getWorldGroupName(UnitTestServerCaller.worldName2));

        // Test with same world and worldgroup again
        command.execute(TEST_USER, new String[]{newWorldGroupName, UnitTestServerCaller.worldName2});

        // Test with invalid / unknown worldgroup
        command.execute(TEST_USER, new String[]{"unknown", UnitTestServerCaller.worldName2});

        // Test with invalid / unknown world
        command.execute(TEST_USER, new String[]{newWorldGroupName, "unknown"});
    }

    @Test
    public void testDelWorldCommand() {
        GroupDelWorldCommand command = new GroupDelWorldCommand("del");

        // Create test world group and add a world to the worldgroup
        final String newWorldGroupName = "worldgroup2";
        Common.getInstance().getWorldGroupManager().addWorldGroup(newWorldGroupName);
        Common.getInstance().getWorldGroupManager().addWorldToGroup(newWorldGroupName, UnitTestServerCaller.worldName2);
        assertEquals(newWorldGroupName, Common.getInstance().getWorldGroupManager().getWorldGroupName(UnitTestServerCaller.worldName2));

        // Test: Remove the world from the group
        command.execute(TEST_USER, new String[]{UnitTestServerCaller.worldName2});
        assertEquals(WorldGroupsManager.DEFAULT_GROUP_NAME, Common.getInstance().getWorldGroupManager().getWorldGroupName(UnitTestServerCaller.worldName2));

        // Test with invalid / unknown world
        command.execute(TEST_USER, new String[]{"unknown"});
    }

    @Test
    public void testCreateCommand() {
        GroupCreateCommand command = new GroupCreateCommand("create");

        final String newWorldGroupName = "worldgroup2";
        assertFalse(Common.getInstance().getWorldGroupManager().worldGroupExist(newWorldGroupName));

        // Test: Create new world group
        command.execute(TEST_USER, new String[]{newWorldGroupName});
        assertTrue(Common.getInstance().getWorldGroupManager().worldGroupExist(newWorldGroupName));

        // Test: World group already exists
        command.execute(TEST_USER, new String[]{newWorldGroupName});
    }

    private PlayerCommandSender createTestUser(String name) {
        UUID uuid = UUID.randomUUID();
        return new PlayerCommandSender<>(name, uuid, new TestCommandSender(uuid, name));
    }
}
