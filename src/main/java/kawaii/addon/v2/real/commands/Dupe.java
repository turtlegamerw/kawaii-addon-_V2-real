package kawaii.addon.v2.real.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;

public class Dupe extends Command {
    public Dupe() {
        super("dupe", "Client-side fake dupe command");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("amount", IntegerArgumentType.integer(1, 64))
            .executes(ctx -> {
                int times = IntegerArgumentType.getInteger(ctx, "amount");
                assert mc.player != null;
                ItemStack stack = mc.player.getMainHandStack();

                if (stack.isEmpty()) {
                    error("You must be holding an item!");
                    return SINGLE_SUCCESS;
                }

                int newCount = Math.min(stack.getCount() * times, stack.getMaxCount());

                ItemStack duped = stack.copy();
                duped.setCount(newCount);

                mc.player.getInventory().setStack(mc.player.getInventory().getSelectedSlot(), duped);
                return SINGLE_SUCCESS;
            })
        );
    }
}
