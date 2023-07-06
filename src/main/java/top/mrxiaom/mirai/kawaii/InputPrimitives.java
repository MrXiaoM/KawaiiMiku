package top.mrxiaom.mirai.kawaii;

import net.mamoe.mirai.internal.deps.io.ktor.utils.io.core.Input;
import net.mamoe.mirai.internal.deps.io.ktor.utils.io.core.InputPrimitivesKt;
import org.jetbrains.annotations.NotNull;

public class InputPrimitives {
    public static long readLong(@NotNull Input input) {
        return InputPrimitivesKt.readLong(input);
    }
}
