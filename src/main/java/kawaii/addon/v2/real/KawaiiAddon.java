package kawaii.addon.v2.real;

import kawaii.addon.v2.real.commands.*;
import kawaii.addon.v2.real.hud.*;
import kawaii.addon.v2.real.modules.*;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class KawaiiAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Kawaii");
    public static final HudGroup HUD_GROUP = new HudGroup("Kawaii");

    @Override
    public void onInitialize() {
        LOG.info("Loading kawaii-addon-_V2-real...");

        // Modules
        Modules.get().add(new ModuleChinaHat());
        Modules.get().add(new ModuleCatFacts());
        Modules.get().add(new HeadPatTurn());

        // Commands
        Commands.add(new CommandCuddle());
        Commands.add(new CommandCrashOut());

        // HUD
        Hud.get().register(HudCat.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "kawaii.addon.v2.real";
    }
}
