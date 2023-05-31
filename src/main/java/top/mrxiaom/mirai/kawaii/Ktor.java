package top.mrxiaom.mirai.kawaii;

import net.mamoe.mirai.internal.deps.io.ktor.utils.io.core.Input;
import net.mamoe.mirai.internal.deps.io.ktor.utils.io.core.InputPrimitivesKt;
import org.jetbrains.annotations.NotNull;

public class Ktor {
    /**
     * 依赖读不到扩展函数 蒸虾头
     */
    public static long readLong(@NotNull Input input) {
        return InputPrimitivesKt.readLong(input);
    }
}
