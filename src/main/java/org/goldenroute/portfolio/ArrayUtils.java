package org.goldenroute.portfolio;

import java.util.Arrays;
import java.util.function.LongFunction;
import java.util.stream.Stream;

public class ArrayUtils
{
    public static Long[] toLong(String[] array)
    {
        Stream<Long> longStream = Arrays.asList(array).stream().mapToLong(Long::parseLong)
                .mapToObj(new LongFunction<Long>()
                {
                    @Override
                    public Long apply(long value)
                    {
                        return Long.valueOf(value);
                    }
                });

        return longStream.toArray(size -> new Long[size]);
    }
}
