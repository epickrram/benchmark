package com.epickrram.benchmark.tlab;

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


import java.util.Arrays;

public final class BenchmarkParams
{
//    public static final int OBJECT_COUNT = 524288;
    public static final int OBJECT_COUNT = 32768;
    public static final int DATA_BUFFER_SIZE = 2048;
    public static final byte[] SOURCE = new byte[DATA_BUFFER_SIZE - 10];

    static
    {
        Arrays.fill(SOURCE, (byte) 7);
    }
}
