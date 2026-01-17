package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.util.Identifier;

public class MapCensor extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public MapCensor() {
        super(KawaiiAddon.CATEGORY, "map-censor", "Replaces maps with a picture.");
    }

    public final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("picture")
        .description("Which image to display on the map.")
        .defaultValue(Mode.pileton)
        .build()
    );

    public enum Mode {
        Rem, rip, punkalopi, pileton, catgirl
    }

    public Identifier getTexture() {
        return switch (mode.get()) {
            case catgirl -> Identifier.of("kawaii-addon", "censor/catgirl.png");
            case pileton -> Identifier.of("kawaii-addon", "censor/pileton.png");
            case punkalopi -> Identifier.of("kawaii-addon", "censor/punkalopi.png");
            case rip -> Identifier.of("kawaii-addon", "censor/rip.png");
            case Rem -> Identifier.of("kawaii-addon", "censor/rem.png");
        };
    }
}
