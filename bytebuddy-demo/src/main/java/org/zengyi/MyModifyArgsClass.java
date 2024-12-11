package org.zengyi;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.util.Arrays;

public class MyModifyArgsClass {

    @RuntimeType
    public Object modifyArgs(
            @AllArguments Object[] args,
            @Morph MyCallable zuper // 支持动态修改入参
    ) {
        System.out.println(Arrays.toString(args));
        Object call = null;
        if (args != null && args.length > 0) {
            args[0] = Integer.valueOf(args[0].toString()) + 10;
        }
        call = zuper.call(args);
        return call;
    }
}
