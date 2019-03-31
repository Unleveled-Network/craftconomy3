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

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.TestCommandSender;
import com.greatmancode.craftconomy3.TestInitializator;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.commands.money.*;
import com.greatmancode.craftconomy3.groups.WorldGroupsManager;
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
    public void testMainCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        MainCommand command = new MainCommand("money");
        command.execute(TEST_USER, new String[]{});
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
    public void testTakeCommand() {
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        Account testAccount = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        final double initialValue = 10000;
        testAccount.set(initialValue, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");
        TakeCommand command = new TakeCommand("take");
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200"});
        assertEquals(initialValue - 200, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "di3"});
        assertEquals(initialValue - 200, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "200"});
        assertEquals(initialValue - 200, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Try to take more money than the account has
        // TODO: Maybe 999999 is too big or there is something wrong with the permissions
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "999999"});
        assertEquals(initialValue - 200, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Currency exists
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200", defaultCurrencyName});
        assertEquals(initialValue - 400, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Currency does not exist
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200", "fake"});
        assertEquals(initialValue - 400, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // World exists
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200", defaultCurrencyName, "UnitTestWorld"});
        assertEquals(initialValue - 600, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // World does not exist
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "200", defaultCurrencyName, "MyCustomWorldWithALongName"});
        assertEquals(initialValue - 600, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
    }

    @Test
    public void testInfiniteCommand() {
        InfiniteCommand command = new InfiniteCommand("infinite");
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        // Test with unknown user
        command.execute(TEST_USER, new String[]{"unknown"});

        // Create test account
        Account testAccount = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        testAccount.set(10000, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        // Turn on for user
        command.execute(TEST_USER, new String[]{TEST_USER.getName()});
        assertTrue(testAccount.hasInfiniteMoney());
        final double balanceBeforeWithInfiniteMoney = testAccount.getBalance("UnitTestWorld", defaultCurrencyName);
        TakeCommand takeCommandWithInfiniteMoney = new TakeCommand("take");
        takeCommandWithInfiniteMoney.execute(TEST_USER2, new String[]{TEST_USER.getName(), "100", defaultCurrencyName, "TestUnitWorld"});
        assertEquals(balanceBeforeWithInfiniteMoney, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);

        // Turn off for user
        command.execute(TEST_USER, new String[]{TEST_USER.getName()});
        assertFalse(testAccount.hasInfiniteMoney());
        final double balanceBeforeWithoutInfiniteMoney = testAccount.getBalance("UnitTestWorld", defaultCurrencyName);
        TakeCommand takeCommandWithoutInfiniteMoney = new TakeCommand("take");
        takeCommandWithoutInfiniteMoney.execute(TEST_USER2, new String[]{TEST_USER.getName(), "100", defaultCurrencyName, "TestUnitWorld"});
        assertEquals(balanceBeforeWithoutInfiniteMoney, testAccount.getBalance("UnitTestWorld", defaultCurrencyName), 100);
    }

    @Test
    public void testSetCommand() {
        SetCommand command = new SetCommand("set");
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        Account testAccount1 = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        Account testAccount2 = Common.getInstance().getAccountManager().getAccount(TEST_USER2.getName(), false);
        double initialValue = 1000;
        testAccount1.set(initialValue, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");
        testAccount2.set(initialValue, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        // Test with own account name
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "100"});
        assertEquals(100, testAccount1.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Test with other account name
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "100"});
        assertEquals(100, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Test with invalid account name
        command.execute(TEST_USER, new String[]{"unknownAccount", "200"});
        assertEquals(100, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);

        // Test with invalid amount
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "abc"});
        assertEquals(100, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);

        // Test with currency name
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "300", defaultCurrencyName});
        assertEquals(300, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Test with invalid currency name
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "400", "unknowncurrency"});
        assertEquals(300, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);

        // Test with world name
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "500", defaultCurrencyName, "UnitTestWorld"});
        assertEquals(500, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Test with invalid world name
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "600", defaultCurrencyName, "invalidworld"});
        assertEquals(500, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);
    }

    @Test
    public void testAllCommand() {
        // Create test accounts
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        Common.getInstance().getAccountManager().getAccount(TEST_USER2.getName(), false);

        AllCommand command = new AllCommand("all");
        command.execute(TEST_USER, new String[]{});
    }

    @Test
    public void testTopCommand() {
        // Create test accounts
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        Common.getInstance().getAccountManager().getAccount(TEST_USER2.getName(), false);

        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        TopCommand command = new TopCommand("top");
        // Test without currency
        command.execute(TEST_USER, new String[]{});
        // Test with default currency
        command.execute(TEST_USER, new String[]{defaultCurrencyName});
        // Test with invalid currency
        command.execute(TEST_USER, new String[]{"unkowncurrency"});

        // Test with page number
        command.execute(TEST_USER, new String[]{defaultCurrencyName, "1"});
        // Test with invalid page number
        command.execute(TEST_USER, new String[]{defaultCurrencyName, "abc"});

        // Test with page number and world name
        command.execute(TEST_USER, new String[]{defaultCurrencyName, "1", WorldGroupsManager.DEFAULT_GROUP_NAME});
        // Test with page number and invalid world name
        command.execute(TEST_USER, new String[]{defaultCurrencyName, "abc", "unknownworldgroup"});
    }


    @Test
    public void testPayCommand() {
        PayCommand command = new PayCommand("pay");
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        Account testAccount1 = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        Account testAccount2 = Common.getInstance().getAccountManager().getAccount(TEST_USER2.getName(), false);
        double initialValue = 1000;
        testAccount1.set(initialValue, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");
        testAccount2.set(initialValue, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        // Test with own account name
        command.execute(TEST_USER, new String[]{TEST_USER.getName(), "100"});
        assertEquals(initialValue, testAccount1.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Test with invalid account name
        command.execute(TEST_USER, new String[]{"unknownAccount", "200"});
        assertEquals(initialValue, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Test with other account name
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "100"});
        assertEquals(initialValue - 100, testAccount1.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        assertEquals(initialValue + 100, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);

        // Test with invalid amount
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "abc"});
        assertEquals(initialValue - 100, testAccount1.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        assertEquals(initialValue + 100, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);

        // Test with currency name
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "300", defaultCurrencyName});
        assertEquals(initialValue - 400, testAccount1.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        assertEquals(initialValue + 400, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Test with invalid currency name
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "400", "unknowncurrency"});
        assertEquals(initialValue - 400, testAccount1.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        assertEquals(initialValue + 400, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);

        // Test with too high amount
        command.execute(TEST_USER, new String[]{TEST_USER2.getName(), "5000", defaultCurrencyName, "UnitTestWorld"});
        assertEquals(initialValue - 400, testAccount1.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        assertEquals(initialValue + 400, testAccount2.getBalance("UnitTestWorld", defaultCurrencyName), 0);
    }

    private PlayerCommandSender createTestUser(String name) {
        UUID uuid = UUID.randomUUID();
        return new PlayerCommandSender<>(name, uuid, new TestCommandSender(uuid, name));
    }
}
