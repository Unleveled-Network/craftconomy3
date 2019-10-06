package com.greatmancode.craftconomy3.utils;

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.tools.entities.Player;
import com.greatmancode.craftconomy3.tools.utils.VaultEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;

public class VaultEconomyProvider extends VaultEconomy {

    @Override
    public boolean hasAccount(Player player) {
        return Common.getInstance().getAccountManager().exist(player.getName(), false);
    }

    @Override
    public double getBalance(Player player, String world) {
        if (!Common.getInstance().getAccountManager().exist(player.getName(), false)) {
            return 0.0;
        }
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        return Common.getInstance().getAccountManager().getAccount(player.getName(), false).getBalance(world, defaultCurrencyName);
    }

    @Override
    public boolean has(Player player, String world, double amount) {
        if (!Common.getInstance().getAccountManager().exist(player.getName(), false)) {
            return false;
        }
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        return Common.getInstance().getAccountManager().getAccount(player.getName(), false).hasEnough(amount, world, defaultCurrencyName);
    }

    @Override
    public EconomyResponse withdrawPlayer(Player p, String world, double amount) {
        if (!Common.getInstance().getAccountManager().exist(p.getName(), false)) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "No account");
        }
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        Account account = Common.getInstance().getAccountManager().getAccount(p.getName(), false);
        double newBalance = account.withdraw(amount, world, defaultCurrencyName, Cause.VAULT, "Withdrawn via Vault");
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(Player player, String world, double amount) {
        if (!Common.getInstance().getAccountManager().exist(player.getName(), false)) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "No account");
        }
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
        Account account = Common.getInstance().getAccountManager().getAccount(player.getName(), false);
        double newBalance = account.deposit(amount, world, defaultCurrencyName, Cause.VAULT, "Deposited via Vault");
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * Creates a bank account with the specified name and the player as the owner.
     * This does not take the money for creating a bank account from the player.
     *
     * @param name   of account
     * @param player the account should be linked to
     * @return EconomyResponse Object
     */
    @Override
    public EconomyResponse createBank(String name, Player player) {
        if (Common.getInstance().getAccountManager().exist(name, true)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bankaccount already exists");
        }

        /*
        Enable this code again to let the player pay the amount of money he needs to buy a bank account.


        Account playersCashAccount = Common.getInstance().getAccountManager().getAccount(player.getName(), false);

        double bankPrice = Common.getInstance().getBankPrice();
        String worldName = Common.getInstance().getServerCaller().getPlayerCaller().getPlayerWorld(player.getUuid());
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();


        // Check if the player has enough money to create a bank account
        if (!playersCashAccount.hasEnough(bankPrice, worldName, defaultCurrencyName)) {
            return new EconomyResponse(bankPrice, 0, EconomyResponse.ResponseType.FAILURE, "Not enough money to create bankaccount");
        }

        // Let the player pay the money to create the bankaccount
        double newCashBalance = playersCashAccount.withdraw(bankPrice, worldName, defaultCurrencyName, Cause.BANK_CREATION, null);
        */

        // Get the bank account and set the permissions
        Account playersBankAccount = Common.getInstance().getAccountManager().getAccount(name, true);
        playersBankAccount.getAccountACL().set(player.getName(), true, true, true, true, true);

        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * Check if a player is the owner of a bank account
     *
     * @param name   of the account
     * @param player to check for ownership
     * @return EconomyResponse Object With Success if the account (with the given name) exists and the given player has owner permissions to it.
     */
    @Override
    public EconomyResponse isBankOwner(String name, Player player) {
        if (!Common.getInstance().getAccountManager().exist(name, true)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not exist");
        }

        Account bankAccount = Common.getInstance().getAccountManager().getAccount(name, true);
        if (bankAccount.getAccountACL().isOwner(player.getName())) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player is not the owner of the bank account");
        }
    }

    /**
     * Check if the player is a member of the bank account
     *
     * @param name   of the account
     * @param player to check membership
     * @return EconomyResponse Object With Success if the account (with the given name) exists and the given player can show the balance, deposit money and withdraw money.
     */
    @Override
    public EconomyResponse isBankMember(String name, Player player) {
        if (!Common.getInstance().getAccountManager().exist(name, true)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not exist");
        }

        Account bankAccount = Common.getInstance().getAccountManager().getAccount(name, true);
        if (bankAccount.getAccountACL().canDeposit(player.getName()) &&
                bankAccount.getAccountACL().canShow(player.getName()) &&
                bankAccount.getAccountACL().canWithdraw(player.getName())) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player is not a member of the bank account");
        }
    }

    @Override
    public boolean createPlayerAccount(Player player) {
        if (Common.getInstance().getAccountManager().exist(player.getName(), false)) {
            // Account already exists
            return false;
        }

        // This getAccount() puts the account to the list if it is not already in there
        Common.getInstance().getAccountManager().getAccount(player.getName(), false);

        return true;
    }

    @Override
    public boolean isEnabled() {
        return Common.getInstance() != null && Common.isInitialized();
    }

    @Override
    public String getName() {
        return Common.getInstance().getServerCaller().getPluginName();
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        // TODO Can we get this from the config?
        return 2;
    }

    /**
     * Format amount into a human readable String This provides translation into
     * economy specific formatting to improve consistency between plugins.
     *
     * @param v to format
     * @return Human readable string describing amount
     */
    @Override
    public String format(double v) {
        String worldName = Common.getInstance().getServerCaller().getDefaultWorld();
        Currency defaultCurrency = Common.getInstance().getCurrencyManager().getDefaultCurrency();

        return Common.getInstance().format(worldName, defaultCurrency, v);
    }

    @Override
    public String currencyNamePlural() {
        return Common.getInstance().getCurrencyManager().getDefaultCurrency().getPlural();
    }

    @Override
    public String currencyNameSingular() {
        return Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        if (!Common.getInstance().getAccountManager().exist(s, true)) {
            // Bank account does not even exist
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not exist");
        }

        String worldName = Common.getInstance().getServerCaller().getDefaultWorld();
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        double balance = Common.getInstance().getAccountManager().getAccount(s, true).getBalance(worldName, defaultCurrencyName);
        Common.getInstance().getAccountManager().delete(s, true);

        return new EconomyResponse(balance, 0, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        if (!Common.getInstance().getAccountManager().exist(s, true)) {
            // Bank account does not even exist
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not exist");
        }

        String worldName = Common.getInstance().getServerCaller().getDefaultWorld();
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        double balance = Common.getInstance().getAccountManager().getAccount(s, true).getBalance(worldName, defaultCurrencyName);

        return new EconomyResponse(balance, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        if (!Common.getInstance().getAccountManager().exist(s, true)) {
            // Bank account does not even exist
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not exist");
        }

        String worldName = Common.getInstance().getServerCaller().getDefaultWorld();
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        double balance = Common.getInstance().getAccountManager().getAccount(s, true).getBalance(worldName, defaultCurrencyName);

        if (balance >= v) {
            return new EconomyResponse(v, balance, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(v, balance, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not have enough money");
        }
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        if (!Common.getInstance().getAccountManager().exist(s, true)) {
            // Bank account does not even exist
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not exist");
        }

        String worldName = Common.getInstance().getServerCaller().getDefaultWorld();
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        Account bankAccount = Common.getInstance().getAccountManager().getAccount(s, true);

        if (bankAccount.hasEnough(v, worldName, defaultCurrencyName)) {
            double newBalance = bankAccount.withdraw(v, worldName, defaultCurrencyName, Cause.VAULT, "Withdrawn via Vault");
            return new EconomyResponse(v, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            double balance = bankAccount.getBalance(worldName, defaultCurrencyName);
            return new EconomyResponse(v, balance, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not have enough money");
        }
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        if (!Common.getInstance().getAccountManager().exist(s, true)) {
            // Bank account does not even exist
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bankaccount does not exist");
        }

        String worldName = Common.getInstance().getServerCaller().getDefaultWorld();
        String defaultCurrencyName = Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();

        Account bankAccount = Common.getInstance().getAccountManager().getAccount(s, true);

        double newBalance = bankAccount.deposit(v, worldName, defaultCurrencyName, Cause.VAULT, "Deposited via Vault");
        return new EconomyResponse(v, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public List<String> getBanks() {
        return Common.getInstance().getAccountManager().getAllAccounts(true);
    }
}
