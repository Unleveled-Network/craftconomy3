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
import com.greatmancode.craftconomy3.commands.bank.*;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.tools.caller.unittest.UnitTestPlayerCaller;
import com.greatmancode.tools.commands.PlayerCommandSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class TestBankCommands {

    private static final String BANK_ACCOUNT = "testbankaccount39";
    private static PlayerCommandSender TEST_USER;
    private static PlayerCommandSender TEST_USER2;
    private UUID testUUIDUser = UUID.randomUUID();
    private UUID testUUIDUser2 = UUID.randomUUID();

    @Before
    public void setUp() {
        new TestInitializator();
        System.out.println("Initialized");
        TEST_USER = new PlayerCommandSender<>("testuser39", testUUIDUser, new TestCommandSender(testUUIDUser, "testuser39"));
        TEST_USER2 = new PlayerCommandSender<>("testuser40", testUUIDUser2, new TestCommandSender(testUUIDUser2, "testuser40"));
    }

    @After
    public void close() {
        Common.getInstance().onDisable();
    }

    @Test
    public void testBankCreateCommand() {
        BankCreateCommand command = new BankCreateCommand("create");
        System.out.println("Creating a bank account!");
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT});
        System.out.println("DONE");
        assertFalse(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).set(200, "default", Common
                .getInstance
                        ().getCurrencyManager().getDefaultCurrency().getName(), Cause.USER, "greatman");
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT});
        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT});
    }

    @Test
    public void testBankGiveCommand() {
        BankGiveCommand command = new BankGiveCommand("give");
        Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        double initialValue = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).getBalance("UnitTestWorld", defaultCurrencyName);
        // Account exists
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "wow"});
        assertEquals(initialValue, Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100"});
        assertEquals(initialValue + 100, Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Account does not exist
        command.execute(TEST_USER, new String[]{"doesnotexist", "100"});
        assertEquals(initialValue + 100, Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Currency exists
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", defaultCurrencyName});
        assertEquals(initialValue + 200, Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Currency does not exist
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", "fake"});
        assertEquals(initialValue + 200, Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // World exists
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", defaultCurrencyName, "UnitTestWorld"});
        assertEquals(initialValue + 300, Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // World does not exist
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", defaultCurrencyName, "MyCustomWorldWithALongName"});
        assertEquals(initialValue + 300, Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).getBalance("UnitTestWorld", defaultCurrencyName), 0);
    }

    @Test
    public void testBankTakeCommand() {
        BankTakeCommand command = new BankTakeCommand("take");
        Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        Account bankAccount = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        final double initialValue = 10000;
        bankAccount.set(initialValue, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");
        // Account exists
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "wow"});
        assertEquals(initialValue, bankAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100"});
        assertEquals(initialValue - 100, bankAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Account does not exist
        command.execute(TEST_USER, new String[]{"doesnotexist", "100"});
        assertEquals(initialValue - 100, bankAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Currency exists
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", defaultCurrencyName});
        assertEquals(initialValue - 200, bankAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // Currency does not exist
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", "fake"});
        assertEquals(initialValue - 200, bankAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // World exists
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", defaultCurrencyName, "UnitTestWorld"});
        assertEquals(initialValue - 300, bankAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
        // World does not exist
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", defaultCurrencyName, "MyCustomWorldWithALongName"});
        assertEquals(initialValue - 300, bankAccount.getBalance("UnitTestWorld", defaultCurrencyName), 0);
    }

    @Test
    public void testBankBalanceCommand() {
        BankBalanceCommand command = new BankBalanceCommand("balance");
        Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);

        // Test own balance
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT});

        // Test unknown player's balance
        command.execute(TEST_USER, new String[]{"unknown"});

        // Test other (empty) account balance
        final String otherTestAccountName = "othertestbankaccount";
        Account otherAccount = Common.getInstance().getAccountManager().getAccount(otherTestAccountName, true);
        otherAccount.getAccountACL().set(TEST_USER2.getName(), true, true, true, true, true);
        otherAccount.getAccountACL().set(TEST_USER.getName(), true, true, true, true, false);
        command.execute(TEST_USER, new String[]{otherTestAccountName});

        // Test other account with balance
        final String defaultBankCurrencyName = Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName();
        otherAccount.set(100, "unittestworld", defaultBankCurrencyName, Cause.UNKNOWN, "unittest");
        command.execute(TEST_USER, new String[]{otherTestAccountName});

        // Test the other account without ACL but with permissions
        otherAccount.getAccountACL().set(TEST_USER.getName(), false, false, false, false, false);
        if (Common.getInstance().getServerCaller().getPlayerCaller() instanceof UnitTestPlayerCaller) {
            UnitTestPlayerCaller unitTestPlayerCaller = (UnitTestPlayerCaller) Common.getInstance().getServerCaller().getPlayerCaller();
            unitTestPlayerCaller.addPermission(TEST_USER.getUuid(), "craftconomy.bank.balance.others");

            command.execute(TEST_USER, new String[]{otherTestAccountName});
        }
    }

    @Test
    public void testBankDepositCommand() {
        // Create test bank account
        Currency currency = Common.getInstance().getCurrencyManager().getDefaultCurrency();
        Account account = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        account.set(Common.getInstance().getBankPrice(), "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");
        new BankCreateCommand("create").execute(TEST_USER, new String[]{BANK_ACCOUNT});
        assertEquals(0, account.getBalance("UnitTestWorld", currency.getName()), 0);

        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));
        Account bankAccount = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);

        // Give some money for deposit tests
        account.set(1000, "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");

        // Test command
        BankDepositCommand command = new BankDepositCommand("deposit");

        double accountBalanceBeforeDeposit = account.getBalance("UnitTestWorld", currency.getName());
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100", currency.getName()});
        assertEquals(100, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);
        double accountBalanceAfterDeposit = account.getBalance("UnitTestWorld", currency.getName());
        assertEquals(accountBalanceBeforeDeposit, accountBalanceAfterDeposit, 100);

        // Test without name of the currency
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100"});
        assertEquals(200, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);

        // Test with unkown account
        command.execute(TEST_USER, new String[]{"unknownaccount", "100"});
        assertEquals(200, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);
        // Test without permission
        bankAccount.getAccountACL().set(TEST_USER.getName(), false, false, false, false, false);
        command.execute(TEST_USER, new String[]{"unknownaccount", "100"});
        bankAccount.getAccountACL().set(TEST_USER.getName(), true, true, true, true, true);
        assertEquals(200, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);
        // Test with invalid amount
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "abc"});
        assertEquals(200, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);
        // Test without enough money
        account.set(0, "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100"});
        assertEquals(200, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);


        // Create other test bank account
        Account testAccount2 = Common.getInstance().getAccountManager().getAccount(TEST_USER2.getName(), false);
        testAccount2.set(Common.getInstance().getBankPrice(), "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");
        final String testBankAccountName2 = "othertestbankaccount";
        new BankCreateCommand("create").execute(TEST_USER2, new String[]{testBankAccountName2});
        assertTrue(Common.getInstance().getAccountManager().exist(testBankAccountName2, true));
        Account testBankAccount2 = Common.getInstance().getAccountManager().getAccount(testBankAccountName2, true);
        testBankAccount2.set(100, "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");


        // Test access to other account without ACL and without permissions
        if (Common.getInstance().getServerCaller().getPlayerCaller() instanceof UnitTestPlayerCaller) {
            account.set(100, "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");
            command.execute(TEST_USER, new String[]{testBankAccountName2, "100"});
            assertEquals(100, testBankAccount2.getBalance("UnitTestWorld", currency.getName()), 0);
            assertEquals(100, account.getBalance("UnitTestWorld", currency.getName()), 0);
        }

        // Test access to other account without ACL but with permissions
        if (Common.getInstance().getServerCaller().getPlayerCaller() instanceof UnitTestPlayerCaller) {
            account.set(100, "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");
            UnitTestPlayerCaller unitTestPlayerCaller = (UnitTestPlayerCaller) Common.getInstance().getServerCaller().getPlayerCaller();
            unitTestPlayerCaller.addPermission(TEST_USER.getUuid(), "craftconomy.bank.deposit.others");

            command.execute(TEST_USER, new String[]{testBankAccountName2, "100"});
            assertEquals(200, testBankAccount2.getBalance("UnitTestWorld", currency.getName()), 0);
            assertEquals(0, account.getBalance("UnitTestWorld", currency.getName()), 0);
        }
    }

    @Test
    public void testBankIgnoreACLCommand() {
        Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        BankIgnoreACLCommand command = new BankIgnoreACLCommand("ignoreacl");
        assertFalse(Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).ignoreACL());
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT});
        assertTrue(Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).ignoreACL());
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT});
        assertFalse(Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).ignoreACL());
        command.execute(TEST_USER, new String[]{"unknown"});
        assertFalse(Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true).ignoreACL());
    }

    @Test
    public void testBankListCommand() {
        //Can't use the global-defined accounts since we require the user to have exactly one account after adding it
        String BANK_LIST_USER = "banklistuser",
                BANK_LIST_ACC = "banklistacc";
        Account bank = Common.getInstance().getAccountManager().getAccount(BANK_LIST_ACC, true);
        bank.getAccountACL().set(BANK_LIST_USER, true, true, true, true, true);
        assertEquals(Common.getInstance().getStorageHandler().getStorageEngine().getBankAccountList(BANK_LIST_USER).length, 1);

        BankListCommand command = new BankListCommand("list");
        command.execute(TEST_USER, new String[]{});
    }

    @Test
    public void testBankPermCommand() {
        BankPermCommand command = new BankPermCommand("permission");
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).set(200, "default", Common.getInstance
                ().getCurrencyManager().getDefaultCurrency().getName(), Cause.USER, "greatman");
        new BankCreateCommand("create").execute(TEST_USER, new String[]{BANK_ACCOUNT});
        Account account = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "deposit", TEST_USER2.getName(), "true"});
        System.out.println("WOW SUPER");

        assertTrue(account.getAccountACL().canDeposit(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canAcl(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canShow(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canWithdraw(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().isOwner(TEST_USER2.getName()));
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "deposit", TEST_USER2.getName(), "false"});
        assertFalse(account.getAccountACL().canDeposit(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canAcl(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canShow(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canWithdraw(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().isOwner(TEST_USER2.getName()));

        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "withdraw", TEST_USER2.getName(), "true"});
        assertFalse(account.getAccountACL().canDeposit(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canAcl(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canShow(TEST_USER2.getName()));
        assertTrue(account.getAccountACL().canWithdraw(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().isOwner(TEST_USER2.getName()));

        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "withdraw", TEST_USER2.getName(), "false"});
        assertFalse(account.getAccountACL().canDeposit(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canAcl(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canShow(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canWithdraw(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().isOwner(TEST_USER2.getName()));


        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "show", TEST_USER2.getName(), "true"});
        assertFalse(account.getAccountACL().canDeposit(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canAcl(TEST_USER2.getName()));
        assertTrue(account.getAccountACL().canShow(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canWithdraw(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().isOwner(TEST_USER2.getName()));

        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "show", TEST_USER2.getName(), "false"});
        assertFalse(account.getAccountACL().canDeposit(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canAcl(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canShow(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().canWithdraw(TEST_USER2.getName()));
        assertFalse(account.getAccountACL().isOwner(TEST_USER2.getName()));
    }

    @Test
    public void testBankSetCommand() {
        Currency currency = Common.getInstance().getCurrencyManager().getDefaultCurrency();
        Account bankAccount = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);

        BankSetCommand command = new BankSetCommand("set");
        double initialValue = bankAccount.getBalance("UnitTestWorld", currency.getName());
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "wow"});
        assertEquals(initialValue, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "100"});
        assertEquals(initialValue + 100, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "0"});
        assertEquals(initialValue, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);

        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "1000", currency.getName()});
        assertEquals(1000, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "1111", currency.getName(), "UnitTestWorld"});
        assertEquals(1111, bankAccount.getBalance("UnitTestWorld", currency.getName()), 0);

        // Test world does not exist
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "123", currency.getName(), "UnitTestWorld123"});
        assertNotEquals(123, bankAccount.getBalance("UnitTestWorld123", currency.getName()), 0);

        // Test currency does not exist
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT, "321", "unknownCurrency", "UnitTestWorld"});
        assertNotEquals(321, bankAccount.getBalance("UnitTestWorld123", "unknownCurrency"), 0);
    }

    @Test
    public void testBankWithdrawCommand() {
        BankWithdrawCommand command = new BankWithdrawCommand("withdraw");
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false).set(200, "default", Common.getInstance
                ().getCurrencyManager().getDefaultCurrency().getName(), Cause.USER, "greatman");
        new BankCreateCommand("create").execute(TEST_USER, new String[]{BANK_ACCOUNT});
        Account account = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        account.set(200, "default", Common.getInstance().getCurrencyManager().getDefaultCurrency().getName(), Cause.UNKNOWN, null);
        account.withdraw(20, "default", Common.getInstance().getCurrencyManager().getDefaultCurrency().getName(), Cause.UNKNOWN, null);
        assertEquals(180, account.getBalance("default", Common.getInstance().getCurrencyManager().getDefaultCurrency().getName()), 0);
    }

    @Test
    public void testBankDeleteCommand() {
        // Create test bank account
        Currency currency = Common.getInstance().getCurrencyManager().getDefaultCurrency();
        Account account = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        account.set(Common.getInstance().getBankPrice(), "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");
        new BankCreateCommand("create").execute(TEST_USER, new String[]{BANK_ACCOUNT});
        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));
        Account bankAccount = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        bankAccount.set(100, "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");

        // Delete the account
        BankDeleteCommand command = new BankDeleteCommand("delete");
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT});
        assertFalse(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        // Try to delete account that does not exist
        assertFalse(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));
        command.execute(TEST_USER, new String[]{BANK_ACCOUNT});
        assertFalse(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));


        // Create other test bank account
        Account testAccount2 = Common.getInstance().getAccountManager().getAccount(TEST_USER2.getName(), false);
        testAccount2.set(Common.getInstance().getBankPrice(), "UnitTestWorld", currency.getName(), Cause.UNKNOWN, "Unittest");
        final String testBankAccountName2 = "othertestbankaccount";
        new BankCreateCommand("create").execute(TEST_USER2, new String[]{testBankAccountName2});
        assertTrue(Common.getInstance().getAccountManager().exist(testBankAccountName2, true));

        // Test access to other account without ACL and without permissions
        if (Common.getInstance().getServerCaller().getPlayerCaller() instanceof UnitTestPlayerCaller) {
            command.execute(TEST_USER, new String[]{testBankAccountName2});
            assertTrue(Common.getInstance().getAccountManager().exist(testBankAccountName2, true));
        }

        // Test access to other account without ACL but with permissions
        if (Common.getInstance().getServerCaller().getPlayerCaller() instanceof UnitTestPlayerCaller) {
            UnitTestPlayerCaller unitTestPlayerCaller = (UnitTestPlayerCaller) Common.getInstance().getServerCaller().getPlayerCaller();
            unitTestPlayerCaller.addPermission(TEST_USER.getUuid(), "craftconomy.bank.delete.admin");

            command.execute(TEST_USER, new String[]{testBankAccountName2});
            assertFalse(Common.getInstance().getAccountManager().exist(testBankAccountName2, true));
        }
    }
}
