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
import com.greatmancode.craftconomy3.commands.money.*;
import com.greatmancode.tools.commands.PlayerCommandSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by greatman on 2014-07-02.
 */
public class TestMoneyCommands {

    private static PlayerCommandSender TEST_USER;
    private static PlayerCommandSender TEST_USER2;

    @Before
    public void setUp() {
        new TestInitializator();
        TEST_USER = createTestUser("testuser39");
        TEST_USER2 = createTestUser("testuser40");
    }

    @After
    public void close() {
        Common.getInstance().onDisable();
    }

    @Test
    public void testBalanceCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        BalanceCommand command = new BalanceCommand(null);
        // User exists
        command.execute(TEST_USER, new String[]{TEST_USER.getName()});
        // User does not exist
        command.execute(TEST_USER, new String[]{"unknown"});
        // No user given
        command.execute(TEST_USER, new String[]{});
    }

    @Test
    public void testCreateCommand() {
        CreateCommand command = new CreateCommand(null);
        command.execute(TEST_USER, new String[]{"testaccount"});
        assertTrue(Common.getInstance().getAccountManager().exist("testaccount", false));
        command.execute(TEST_USER, new String[]{"testaccount"});
    }

    @Test
    public void testDeleteCommand() {
        Common.getInstance().getAccountManager().getAccount("testaccount", false);
        DeleteCommand command = new DeleteCommand(null);
        // Account exists
        command.execute(TEST_USER, new String[]{"testaccount"});
        // Account should no longer exist
        assertFalse(Common.getInstance().getAccountManager().exist("testaccount", false));
        // Check again with an account that never existed
        command.execute(TEST_USER, new String[]{"testaccountwithaverylongname"});
    }

    @Test
    public void testGiveCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        double initialValue = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).getBalance("UnitTestWorld", defaultCurrencyName);
        GiveCommand command = new GiveCommand(null);
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200"});
        assertEquals(initialValue + 200, Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "di3"});
        assertEquals(initialValue + 200, Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "200"});
        assertEquals(initialValue + 200, Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Currency exists
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200", defaultCurrencyName});
        assertEquals(initialValue + 400, Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Currency does not exist
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200", "fake"});
        assertEquals(initialValue + 400, Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // World exists
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200", defaultCurrencyName, "UnitTestWorld"});
        assertEquals(initialValue + 600, Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // World does not exist
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200", defaultCurrencyName, "MyCustomWorldWithALongName"});
        assertEquals(initialValue + 600, Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).getBalance("UnitTestWorld", defaultCurrencyName), 0);
    }

    @Test
    public void testInfiniteCommand() {
        // TODO Test if this worked (with accountmanager)

        InfiniteCommand command = new InfiniteCommand("infinite");

        // Test with unknown user
        command.execute(TEST_USER, new String[]{"unknown"});

        // Turn on for user
        command.execute(TEST_USER, new String[]{TEST_USER.getName()});
        // Turn off for user
        command.execute(TEST_USER, new String[]{TEST_USER.getName()});
    }

    @Test
    public void testSetCommand() {
        // TODO Test if this worked (with accountmanager)

        SetCommand command = new SetCommand("set");
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        // Test without amount
        command.execute(TEST_USER, new String[]{TEST_USER.getName()});

        // Test with own user
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "100"});
        // Test with own user and default currency
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "100", defaultCurrencyName});

        // Test with other user
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "100"});
        // Test with other user and default currency
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "100", defaultCurrencyName});
    }

    private PlayerCommandSender createTestUser(String name) {
        UUID uuid = UUID.randomUUID();
        return new PlayerCommandSender<>(name, uuid, new TestCommandSender(uuid, name));
    }
}
