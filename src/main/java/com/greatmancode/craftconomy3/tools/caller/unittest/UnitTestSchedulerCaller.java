/**
 * This file is part of GreatmancodeTools.
 * <p>
 * Copyright (c) 2013-2016, Greatman <http://github.com/greatman/>
 * <p>
 * GreatmancodeTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * GreatmancodeTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with GreatmancodeTools.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.tools.caller.unittest;

import com.greatmancode.craftconomy3.tools.interfaces.caller.SchedulerCaller;
import com.greatmancode.craftconomy3.tools.interfaces.caller.ServerCaller;

public class UnitTestSchedulerCaller extends SchedulerCaller {
    public UnitTestSchedulerCaller(ServerCaller caller) {
        super(caller);
    }

    @Override
    public int schedule(Runnable entry, long firstStart, long repeating) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int schedule(Runnable entry, long firstStart, long repeating, boolean async) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void cancelSchedule(int id) {
        // TODO Auto-generated method stub

    }

    /**
     * Runs the given runnable once after the start time (in seconds) is over.
     *
     * @param entry The task to delay
     * @param start When should the task be started? (In seconds)
     * @return
     */
    @Override
    public int delay(Runnable entry, long start) {
        if (start > 0) {
            try {
                Thread.sleep(start * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        entry.run();

        return 0;
    }

    /**
     * Runs the given runnable atleast once.
     *
     * @param entry The task to delay
     * @param start When should the task be started? (In seconds)
     * @param async Should the task be Async? (Threaded)
     * @return
     */
    @Override
    public int delay(Runnable entry, long start, boolean async) {
        // TODO Run async

        return this.delay(entry, start);
    }
}
