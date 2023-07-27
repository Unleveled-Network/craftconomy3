/*
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018-2019, Pavog <http://github.com/pavog/>
 *
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3. If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.commands.money;

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.entities.Player;
import com.greatmancode.craftconomy3.tools.utils.Tools;

public class GiveCommand extends AbstractCommand {
    public GiveCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (Common.getInstance().getAccountManager().exist(args[0], false)) {

            if (Tools.isValidDouble(args[1])) {
                double amount = Double.parseDouble(args[1]);
                Currency currency = Common.getInstance().getCurrencyManager().getDefaultCurrency();
                if (args.length > 2) {
                    currency = checkCurrencyExists(sender, args[2]);
                    if (currency == null) return;
                }
                String worldName = Account.getWorldGroupOfPlayerCurrentlyIn(sender.getUuid());
                if (args.length > 3) {
                    if (!checkWorldExists(sender, args[3])) return;
                    worldName = Common.getInstance().getWorldGroupManager().getWorldGroupName(args[3]);
                }
                Account account = Common.getInstance().getAccountManager().getAccount(args[0], false);
                account.deposit(amount, worldName, currency.getName(), Cause.USER, sender.getName());
                sendMessage(sender, Common.getInstance().getLanguageManager().parse("money_give_send", Common.getInstance().format(worldName, currency, amount), args[0]));
                if (!Common.getInstance().getMainConfig().getBoolean("System.SilentGiveCommand", false)) {
                    Player reciever = Common.getInstance().getServerCaller().getPlayerCaller().getOnlinePlayer(args[0]);
                    if (reciever != null) {
                        Common.getInstance().getServerCaller().getPlayerCaller().sendMessage(reciever.getUuid(), Common
                                .getInstance().getLanguageManager().parse("money_give_received", Common.getInstance().format
                                        (worldName, currency, amount), sender.getName()));
                    }
                }
            } else {
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("invalid_amount"));
            }
        } else {
            sendMessage(sender, Common.getInstance().getLanguageManager().getString("account_not_exist"));
        }
    }

    @Override
    public String help() {
        return Common.getInstance().getLanguageManager().getString("money_give_cmd_help");
    }

    @Override
    public int maxArgs() {
        return 4;
    }

    @Override
    public int minArgs() {
        return 2;
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermissionNode() {
        return "craftconomy.money.give";
    }
}
