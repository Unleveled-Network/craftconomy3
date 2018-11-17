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
package com.greatmancode.craftconomy3.commands;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.TestInitializator;
import com.greatmancode.craftconomy3.commands.money.BalanceCommand;
import com.greatmancode.craftconomy3.commands.money.CreateCommand;
import com.greatmancode.craftconomy3.commands.money.DeleteCommand;
import com.greatmancode.craftconomy3.commands.money.GiveCommand;
import com.greatmancode.craftconomy3.commands.setup.NewSetupBasicCommand;
import com.greatmancode.craftconomy3.commands.setup.NewSetupConvertCommand;
import com.greatmancode.craftconomy3.commands.setup.NewSetupCurrencyCommand;
import com.greatmancode.craftconomy3.commands.setup.NewSetupDatabaseCommand;
import com.greatmancode.tools.commands.PlayerCommandSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

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
        NewSetupBasicCommand command = new NewSetupBasicCommand();
        command.execute(TEST_USER, new String[]{});
    }

    @Test
    public void testSetupConvertCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        NewSetupConvertCommand command = new NewSetupConvertCommand();
        command.execute(TEST_USER, new String[]{});
    }

    @Test
    public void testSetupCurrencyCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        NewSetupCurrencyCommand command = new NewSetupCurrencyCommand();
        command.execute(TEST_USER, new String[]{ "name", "test" });
        command.execute(TEST_USER, new String[]{ "nameplural", "tests" });
        command.execute(TEST_USER, new String[]{ "minor", "teston" });
        command.execute(TEST_USER, new String[]{ "minorplural", "testoni" });
        command.execute(TEST_USER, new String[]{ "sign", "T" });
    }

    @Test
    public void testSetupDatabaseCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        NewSetupDatabaseCommand command = new NewSetupDatabaseCommand();
        command.execute(TEST_USER, new String[]{});
    }


    private PlayerCommandSender createTestUser(String name) {
        return new PlayerCommandSender(name, UUID.randomUUID());
    }
}
