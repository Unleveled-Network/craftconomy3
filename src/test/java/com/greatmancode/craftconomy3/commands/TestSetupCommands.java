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
import com.greatmancode.craftconomy3.commands.setup.SetupBasicCommand;
import com.greatmancode.craftconomy3.commands.setup.SetupConvertCommand;
import com.greatmancode.craftconomy3.commands.setup.SetupCurrencyCommand;
import com.greatmancode.craftconomy3.commands.setup.SetupDatabaseCommand;
import com.greatmancode.craftconomy3.tools.commands.PlayerCommandSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by pavog on 2018-11-17.
 */
public class TestSetupCommands {

    private static PlayerCommandSender TEST_USER;

    @Before
    public void setUp() {
        new TestInitializator();
        TEST_USER = createTestUser("testuser41");
    }

    @After
    public void close() {
        Common.getInstance().onDisable();
    }

    @Test
    public void testSetupBasicCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        SetupBasicCommand command = new SetupBasicCommand(null);
        command.execute(TEST_USER, new String[]{});
    }

    @Test
    public void testSetupConvertCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        SetupConvertCommand command = new SetupConvertCommand(null);
        command.execute(TEST_USER, new String[]{});
    }

    @Test
    public void testSetupCurrencyCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        SetupCurrencyCommand command = new SetupCurrencyCommand(null);
        command.execute(TEST_USER, new String[]{"name", "test"});
        command.execute(TEST_USER, new String[]{"nameplural", "tests"});
        command.execute(TEST_USER, new String[]{"minor", "teston"});
        command.execute(TEST_USER, new String[]{"minorplural", "testoni"});
        command.execute(TEST_USER, new String[]{"sign", "T"});
        // Invalid step. Should send "{{DARK_RED}}Invalid sub-step! Please write a valid one.".
        command.execute(TEST_USER, new String[]{"test", "invalid"});
    }

    @Test
    public void testSetupDatabaseCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        SetupDatabaseCommand command = new SetupDatabaseCommand(null);
        command.execute(TEST_USER, new String[]{});
    }

    private PlayerCommandSender createTestUser(String name) {
        UUID uuid = UUID.randomUUID();
        return new PlayerCommandSender<>(name, uuid, new TestCommandSender(uuid, name));
    }
}
