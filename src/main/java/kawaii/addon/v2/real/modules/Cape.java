package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

public class Cape extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    public final Setting<Cape> cape = sgGeneral.add(new EnumSetting.Builder<Cape>()
        .name("Cape")
        .defaultValue(Cape.cape1)
        .build()
    );

    public Cape() {
        super(KawaiiAddon.CATEGORY, "Capes", "An example module in a custom category.");
    }

    private final SettingGroup sgCapes = settings.getDefaultGroup();

    public enum Cape{
        cape1,
        cape2,
        cape3,
        cape4
    }
}
