package kawaii.addon.v2.real.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class CrashOut extends Command {
    public CrashOut() {
        super("crashout", "No more ragebait.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            MinecraftClient.getInstance().scheduleStop();
            return SINGLE_SUCCESS;
        });
    }
}
