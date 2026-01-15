package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.Capes;

public class Cape extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    public final Setting<Capes> capes = sgGeneral.add(new EnumSetting.Builder<Capes>()
        .name("Cape")
        .defaultValue(Capes.cape1)
        .build()
    );

    public Cape() {
        super(KawaiiAddon.CATEGORY, "Capes", "Get a cape (client-side only).");
    }

    private final SettingGroup sgCapes = settings.getDefaultGroup();

    public enum Capes{
        cape1, cape2, cape3, cape4, hutao, vape
    }
}