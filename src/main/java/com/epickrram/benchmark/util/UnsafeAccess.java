package com.epickrram.benchmark.util;

//////////////////////////////////////////////////////////////////////////////////
//   Copyright 2014   Mark Price     mark at epickrram.com                      //
//                                                                              //
//   Licensed under the Apache License, Version 2.0 (the "License");            //
//   you may not use this file except in compliance with the License.           //
//   You may obtain a copy of the License at                                    //
//                                                                              //
//       http://www.apache.org/licenses/LICENSE-2.0                             //
//                                                                              //
//   Unless required by applicable law or agreed to in writing, software        //
//   distributed under the License is distributed on an "AS IS" BASIS,          //
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   //
//   See the License for the specific language governing permissions and        //
//   limitations under the License.                                             //
//////////////////////////////////////////////////////////////////////////////////


import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeAccess
{
    private static final sun.misc.Unsafe UNSAFE;

    static {
        try {
            Field unsafe = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            unsafe.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) unsafe.get(null);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private UnsafeAccess() {}

    public static Unsafe getUnsafe()
    {
        return UNSAFE;
    }
}
