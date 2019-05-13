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
package com.greatmancode.craftconomy3.vault;

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.TestCommandSender;
import com.greatmancode.craftconomy3.TestInitializator;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.utils.VaultEconomyProvider;
import com.greatmancode.tools.commands.PlayerCommandSender;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class TestVault {

    private Economy economy;

    private static final String BANK_ACCOUNT = "testbankaccount39";
    private static PlayerCommandSender TEST_USER;
    private static PlayerCommandSender TEST_USER2;

    @Before
    public void setUp() {
        new TestInitializator();
        System.out.println("Initialized");
        UUID testUUIDUser = UUID.randomUUID();
        TEST_USER = new PlayerCommandSender<>("testuser39", testUUIDUser, new TestCommandSender(testUUIDUser, "testuser39"));
        UUID testUUIDUser2 = UUID.randomUUID();
        TEST_USER2 = new PlayerCommandSender<>("testuser40", testUUIDUser2, new TestCommandSender(testUUIDUser2, "testuser40"));
        this.economy = new VaultEconomyProvider();
    }

    @After
    public void close() {
        Common.getInstance().onDisable();
    }

    @Test
    public void testHasBankSupport() {
        assertTrue(this.economy.hasBankSupport());
    }

    @Test
    public void testFractionalDigits() {
        assertEquals(2, this.economy.fractionalDigits());
    }

    @Test
    public void testFormat() {
        // TODO
    }

    @Test
    public void testCurrencyNamePlural() {
        assertEquals(Common.getInstance().getCurrencyManager().getDefaultCurrency().getPlural(), this.economy.currencyNamePlural());
    }

    @Test
    public void testCurrencyNameSingular() {
        assertEquals(Common.getInstance().getCurrencyManager().getDefaultCurrency().getName(), this.economy.currencyNameSingular());
    }

    @Test
    public void testHasAccount() {
        MockedOfflinePlayer mockedOfflinePlayer = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());

        // Account does not exist
        assertFalse(Common.getInstance().getAccountManager().exist(TEST_USER.getName(), false));
        assertFalse(this.economy.hasAccount(mockedOfflinePlayer));

        // Create account
        Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        assertTrue(Common.getInstance().getAccountManager().exist(TEST_USER.getName(), false));
        assertTrue(this.economy.hasAccount(mockedOfflinePlayer));
    }

    @Test
    public void testGetBalance() {
        final String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        MockedOfflinePlayer mockedOfflinePlayer = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());

        // Create test account and set its balance
        Account account = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        account.set(100.0D, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        assertEquals(100.0D, this.economy.getBalance(mockedOfflinePlayer), 0.0D);
        assertEquals(100.0D, this.economy.getBalance(mockedOfflinePlayer, "UnitTestWorld"), 0.0D);

        // Test default value (0.0) without creating an account
        MockedOfflinePlayer mockedOfflinePlayer2 = new MockedOfflinePlayer(TEST_USER2.getUuid(), TEST_USER2.getName());
        assertEquals(0.0D, this.economy.getBalance(mockedOfflinePlayer2), 0.0D);
        assertEquals(0.0D, this.economy.getBalance(mockedOfflinePlayer2, "UnitTestWorld"), 0.0D);
    }

    @Test
    public void testHas() {
        final String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        MockedOfflinePlayer mockedOfflinePlayer = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());

        // Create test account and set its balance
        Account account = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        account.set(100.0D, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        assertTrue(this.economy.has(mockedOfflinePlayer, 50.0D));
        assertTrue(this.economy.has(mockedOfflinePlayer, "UnitTestWorld", 50.0D));
        assertFalse(this.economy.has(mockedOfflinePlayer, 500.0D));
        assertFalse(this.economy.has(mockedOfflinePlayer, "UnitTestWorld", 500.0D));

        // Test default (false) without creating an account
        MockedOfflinePlayer mockedOfflinePlayer2 = new MockedOfflinePlayer(TEST_USER2.getUuid(), TEST_USER2.getName());
        assertFalse(this.economy.has(mockedOfflinePlayer2, 50.0D));
        assertFalse(this.economy.has(mockedOfflinePlayer2, "UnitTestWorld", 50.0D));
    }

    @Test
    public void testWithdrawPlayer() {
        final String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        MockedOfflinePlayer mockedOfflinePlayer = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());
        MockedOfflinePlayer mockedOfflinePlayer2 = new MockedOfflinePlayer(TEST_USER2.getUuid(), TEST_USER2.getName());

        // Create test account and set its balance
        Account account = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        account.set(100.0D, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        EconomyResponse response1 = this.economy.withdrawPlayer(mockedOfflinePlayer, 10.0D);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response1.type);
        assertEquals(90.0D, response1.balance, 0.0D);

        EconomyResponse response2 = this.economy.withdrawPlayer(mockedOfflinePlayer, "UnitTestWorld", 10.0D);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response2.type);
        assertEquals(80.0D, response2.balance, 0.0D);


        // Test default (Error response) without creating an account
        EconomyResponse errorResponse1 = this.economy.withdrawPlayer(mockedOfflinePlayer2, 10.0D);
        assertEquals(EconomyResponse.ResponseType.FAILURE, errorResponse1.type);
        assertEquals(0.0D, errorResponse1.balance, 0.0D);

        EconomyResponse errorResponse2 = this.economy.withdrawPlayer(mockedOfflinePlayer2, "UnitTestWorld", 10.0D);
        assertEquals(EconomyResponse.ResponseType.FAILURE, errorResponse2.type);
        assertEquals(0.0D, errorResponse2.balance, 0.0D);
    }

    @Test
    public void testDepositPlayer() {
        final String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        MockedOfflinePlayer mockedOfflinePlayer = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());
        MockedOfflinePlayer mockedOfflinePlayer2 = new MockedOfflinePlayer(TEST_USER2.getUuid(), TEST_USER2.getName());

        // Create test account and set its balance
        Account account = Common.getInstance().getAccountManager().getAccount(TEST_USER.getName(), false);
        account.set(100.0D, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        EconomyResponse response1 = this.economy.depositPlayer(mockedOfflinePlayer, 10.0D);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response1.type);
        assertEquals(110.0D, response1.balance, 0.0D);

        EconomyResponse response2 = this.economy.depositPlayer(mockedOfflinePlayer, "UnitTestWorld", 10.0D);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response2.type);
        assertEquals(120.0D, response2.balance, 0.0D);


        // Test default (Error response) without creating an account
        EconomyResponse errorResponse1 = this.economy.depositPlayer(mockedOfflinePlayer2, 10.0D);
        assertEquals(EconomyResponse.ResponseType.FAILURE, errorResponse1.type);
        assertEquals(0.0D, errorResponse1.balance, 0.0D);

        EconomyResponse errorResponse2 = this.economy.depositPlayer(mockedOfflinePlayer2, "UnitTestWorld", 10.0D);
        assertEquals(EconomyResponse.ResponseType.FAILURE, errorResponse2.type);
        assertEquals(0.0D, errorResponse2.balance, 0.0D);
    }

    @Test
    public void testCreateBank() {
        assertFalse(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        final MockedOfflinePlayer accountOwner = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());
        EconomyResponse response = this.economy.createBank(BANK_ACCOUNT, accountOwner);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);

        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        // Test what happens if bank account already exists
        EconomyResponse response2 = this.economy.createBank(BANK_ACCOUNT, accountOwner);
        assertEquals(EconomyResponse.ResponseType.FAILURE, response2.type);
    }


    @Test
    public void testDeleteBank() {
        // Create test account
        Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        // Delete test account
        EconomyResponse response = this.economy.deleteBank(BANK_ACCOUNT);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertFalse(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));
    }


    @Test
    public void testBankBalance() {
        final String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName();

        // Create test account
        Account bankAccount = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        // Set balance of test account
        bankAccount.set(200.0D, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        EconomyResponse response = this.economy.bankBalance(BANK_ACCOUNT);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(200.0D, response.balance, 0D);
    }


    @Test
    public void testBankHas() {
        final String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName();

        // Create test account
        Account bankAccount = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        // Set balance of test account
        bankAccount.set(200.0D, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        EconomyResponse response = this.economy.bankHas(BANK_ACCOUNT, 100.0D);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
    }


    @Test
    public void testBankWithdraw() {
        final String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        // Create test bank account and set its balance
        Account account = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        account.set(100.0D, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        EconomyResponse response = this.economy.bankWithdraw(BANK_ACCOUNT, 10.0D);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(90.0D, response.balance, 0.0D);


        // Test default (Error response) without creating an account
        EconomyResponse errorResponse = this.economy.bankWithdraw("unknown", 10.0D);
        assertEquals(EconomyResponse.ResponseType.FAILURE, errorResponse.type);
        assertEquals(0.0D, errorResponse.balance, 0.0D);
    }


    @Test
    public void testBankDeposit() {
        final String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        // Create test bank account and set its balance
        Account account = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        account.set(100.0D, "UnitTestWorld", defaultCurrencyName, Cause.UNKNOWN, "Unittest");

        EconomyResponse response = this.economy.bankDeposit(BANK_ACCOUNT, 10.0D);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(110.0D, response.balance, 0.0D);


        // Test default (Error response) without creating an account
        EconomyResponse errorResponse = this.economy.bankDeposit("unknown", 10.0D);
        assertEquals(EconomyResponse.ResponseType.FAILURE, errorResponse.type);
        assertEquals(0.0D, errorResponse.balance, 0.0D);
    }


    @Test
    public void testIsBankOwner() {
        MockedOfflinePlayer mockedOfflinePlayer = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());

        // Create test account
        Account testAccount = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        EconomyResponse negativeResponse = this.economy.isBankOwner(BANK_ACCOUNT, mockedOfflinePlayer);
        assertEquals(EconomyResponse.ResponseType.FAILURE, negativeResponse.type);

        testAccount.getAccountACL().set(mockedOfflinePlayer.getName(), true, true, true, true, true);
        EconomyResponse successfullResponse = this.economy.isBankOwner(BANK_ACCOUNT, mockedOfflinePlayer);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, successfullResponse.type);

        // Test account does not exist
        EconomyResponse errorResponse = this.economy.isBankOwner("unknownbankaccount", mockedOfflinePlayer);
        assertEquals(EconomyResponse.ResponseType.FAILURE, errorResponse.type);
    }


    @Test
    public void testIsBankMember() {
        MockedOfflinePlayer mockedOfflinePlayer = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());

        // Create test account
        Account testAccount = Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        EconomyResponse negativeResponse = this.economy.isBankMember(BANK_ACCOUNT, mockedOfflinePlayer);
        assertEquals(EconomyResponse.ResponseType.FAILURE, negativeResponse.type);

        testAccount.getAccountACL().set(mockedOfflinePlayer.getName(), true, true, true, true, false);
        EconomyResponse successfullResponse = this.economy.isBankMember(BANK_ACCOUNT, mockedOfflinePlayer);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, successfullResponse.type);

        // Test account does not exist
        EconomyResponse errorResponse = this.economy.isBankMember("unknownbankaccount", mockedOfflinePlayer);
        assertEquals(EconomyResponse.ResponseType.FAILURE, errorResponse.type);
    }


    @Test
    public void testGetBanks() {
        // Create test account
        Common.getInstance().getAccountManager().getAccount(BANK_ACCOUNT, true);
        assertTrue(Common.getInstance().getAccountManager().exist(BANK_ACCOUNT, true));

        assertEquals(1, this.economy.getBanks().size());
        assertEquals(BANK_ACCOUNT, this.economy.getBanks().get(0));
    }


    @Test
    public void testCreatePlayerAccount() {
        final MockedOfflinePlayer accountOwner = new MockedOfflinePlayer(TEST_USER.getUuid(), TEST_USER.getName());
        assertFalse(Common.getInstance().getAccountManager().exist(accountOwner.getName(), false));

        assertTrue(this.economy.createPlayerAccount(accountOwner));
        assertTrue(Common.getInstance().getAccountManager().exist(accountOwner.getName(), false));

        // Test what happens if bank account already exists
        assertFalse(this.economy.createPlayerAccount(accountOwner));
    }
}
