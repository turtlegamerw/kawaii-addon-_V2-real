package kawaii.addon.v2.real.hud;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Identifier;

public class HudCat extends HudElement {
    public static final HudElementInfo<HudCat> INFO = new HudElementInfo<>(KawaiiAddon.HUD_GROUP, "cat-hud", "Displays a cat icon.", HudCat::new);

    public HudCat() {
        super(INFO);
    }

    private final SettingGroup sg = settings.getDefaultGroup();

    private final Setting<Integer> width = sg.add(new IntSetting.Builder()
        .name("width")
        .description("Stretch the cat in the x axis.")
        .defaultValue(5)
        .min(1)
        .sliderMin(1)
        .sliderMax(20)
        .build()
    );


    private final Setting<Integer> height = sg.add(new IntSetting.Builder()
        .name("height")
        .description("Stretch the cat in the y axis.")
        .defaultValue(5)
        .min(1)
        .sliderMin(1)
        .sliderMax(20)
        .build()
    );

    private final Setting<Picture> mode = sg.add(new EnumSetting.Builder<HudCat.Picture>()
        .name("picture")
        .description("set the picture you want.")
        .defaultValue(Picture.Cat1)
        .build()
    );

    public enum Picture {
        Cat1, Cat2, Cat3, Cat4, Cat5
    }

    private Identifier TEXTURE;

    @Override
    public void render(HudRenderer renderer) {
            switch (mode.get()) {
                case Cat1 -> TEXTURE = Identifier.of("kawaii-addon", "hud/cat1.png");
                case Cat2 -> TEXTURE = Identifier.of("kawaii-addon", "hud/cat2.png");
                case Cat3 -> TEXTURE = Identifier.of("kawaii-addon", "hud/cat3.png");
                case Cat4 -> TEXTURE = Identifier.of("kawaii-addon", "hud/cat4.png");
                case Cat5 -> TEXTURE = Identifier.of("kawaii-addon", "hud/cat5.png");
            }
        int x_width = width.get();
        int y_height = height.get();
        setSize(64 * x_width, 64 * y_height);
        renderer.texture(TEXTURE, x, y, getWidth(), getHeight(), Color.WHITE);
    }
}
