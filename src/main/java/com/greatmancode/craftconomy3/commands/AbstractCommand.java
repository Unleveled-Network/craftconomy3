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
package com.greatmancode.craftconomy3.commands;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.commands.interfaces.CommandExecutor;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 13/12/2018.
 */
public abstract class AbstractCommand extends CommandExecutor {

    public AbstractCommand(String name) {
        super(name);
    }

    protected void sendMessage(CommandSender sender, String message) {
        Common.getInstance().getServerCaller().getPlayerCaller().sendMessage(sender, message, getName());
    }

    protected Currency checkCurrencyExists(CommandSender sender, String currencyName) {
        if (Common.getInstance().getCurrencyManager().getCurrency(currencyName) != null) {
            return Common.getInstance().getCurrencyManager().getCurrency(currencyName);
        } else {
            sendMessage(sender, Common.getInstance().getLanguageManager().getString("currency_not_exist"));
            return null;
        }
    }

    protected boolean checkWorldExists(CommandSender sender, String worldName) {
        if (!Common.getInstance().getServerCaller().worldExist(worldName)) {
            sendMessage(sender, Common.getInstance().getLanguageManager().getString("world_not_exist"));
            return false;
        }
        return true;
    }
}
