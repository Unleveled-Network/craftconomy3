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
package com.greatmancode.craftconomy3.commands.money;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.groups.WorldGroupsManager;
import com.greatmancode.tools.commands.CommandSender;

import java.util.List;

class TopCommandThread implements Runnable {
    public static final int NUMBER_ELEMENTS = 10;

    private final CommandSender sender;
    private final int page;
    private final String worldGroup;
    private final Currency currency;

    public TopCommandThread(CommandSender sender, int page, String worldGroup, Currency currency) {
        this.sender = sender;
        this.page = page;
        this.worldGroup = worldGroup;
        this.currency = currency;
    }

    @Override
    public void run() {
        String ret = Common.getInstance().getLanguageManager().parse("money_top_header", page, worldGroup) + "\n";
        List<TopCommand.TopEntry> list = Common.getInstance().getStorageHandler().getStorageEngine().getTopEntry(page, currency, worldGroup);
        for (int i = 0; i < list.size(); i++) {
            TopCommand.TopEntry entry = list.get(i);
            ret += "" + ((page - 1) * NUMBER_ELEMENTS + i + 1) + ": {{DARK_GREEN}}" + entry.username + " {{WHITE}}" + Common.getInstance().format(null, currency, entry.balance) + "\n";
        }

        Common.getInstance().getServerCaller().getSchedulerCaller().delay(new TopCommandThreadEnd(sender, ret), 0, false);
    }

    class TopCommandThreadEnd implements Runnable {
        private final CommandSender sender;
        private final String ret;

        public TopCommandThreadEnd(CommandSender sender, String ret) {
            this.sender = sender;
            this.ret = ret;
        }

        @Override
        public void run() {
            Common.getInstance().getServerCaller().getPlayerCaller().sendMessage(sender, ret, "top");
        }
    }
}

public class TopCommand extends AbstractCommand {
    public TopCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // Check if we have a currency name or use the default currency
        Currency currency = Common.getInstance().getCurrencyManager().getDefaultCurrency();
        if (args.length > 0) {
            if (Common.getInstance().getCurrencyManager().getCurrency(args[0]) != null) {
                currency = Common.getInstance().getCurrencyManager().getCurrency(args[0]);
            } else {
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("currency_not_exist"));
                return;
            }
        }

        // Check if we have a page or use page 1
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                sendMessage(sender, Common.getInstance()
                        .getLanguageManager().getString("invalid_page"));
                return;
            }
        }

        // Check if we have a world or use the world the player is currently in
        String worldGroupName = Account.getWorldGroupOfPlayerCurrentlyIn(sender.getUuid());
        // If the player is not in a worldgroup and the world / worldgroup is not set
        // we use the default world group name
        if (worldGroupName == null && args.length <= 2) {
            worldGroupName = WorldGroupsManager.DEFAULT_GROUP_NAME;
        }

        if (args.length > 2) {
            if (!Common.getInstance().getServerCaller().worldExist(args[2])) {
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("world_not_exist"));
                return;
            }
            worldGroupName = Common.getInstance().getWorldGroupManager().getWorldGroupName(args[2]);
        }

        Common.getInstance().getServerCaller().getSchedulerCaller().delay(new TopCommandThread(sender, page, worldGroupName, currency), 0, false);
    }

    @Override
    public String help() {
        return Common.getInstance().getLanguageManager().getString("money_top_cmd_help");
    }

    @Override
    public int maxArgs() {
        return 3;
    }

    @Override
    public int minArgs() {
        return 0;
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermissionNode() {
        return "craftconomy.money.top";
    }

    public static class TopEntry {
        public String username;
        public Currency currency;
        public double balance;

        public TopEntry(String username, Currency currency, double balance) {
            this.username = username;
            this.currency = currency;
            this.balance = balance;
        }
    }
}
