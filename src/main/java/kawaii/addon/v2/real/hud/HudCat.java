package kawaii.addon.v2.real.hud;

import kawaii.addon.v2.real.KawaiiAddon;
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
        preloadTextures();
    }

    private final SettingGroup sg = settings.getDefaultGroup();

    // Width slider
    private final Setting<Integer> width = sg.add(new IntSetting.Builder()
        .name("width")
        .description("Stretch the cat in the x axis.")
        .defaultValue(5)
        .min(1)
        .sliderMin(1)
        .sliderMax(20)
        .build()
    );

    // Height slider
    private final Setting<Integer> height = sg.add(new IntSetting.Builder()
        .name("height")
        .description("Stretch the cat in the y axis.")
        .defaultValue(5)
        .min(1)
        .sliderMin(1)
        .sliderMax(20)
        .build()
    );

    // Picture selector 1-5
    private final Setting<Integer> picture = sg.add(new IntSetting.Builder()
        .name("picture")
        .description("Select different pictures of catgirls.")
        .defaultValue(1)
        .min(1)
        .sliderMin(1)
        .sliderMax(5)
        .build()
    );

    // Array to hold preloaded textures
    private static final Identifier[] TEXTURES = new Identifier[5];

    // Preload all textures using a loop
    private static void preloadTextures() {
        for (int i = 0; i < TEXTURES.length; i++) {
            // No leading / in path
            TEXTURES[i] = Identifier.of("kawaii-addon", "hud/cat" + (i + 1) + ".png");
        }
    }

    @Override
    public void render(HudRenderer renderer) {
        int x_width = width.get();
        int y_height = height.get();
        setSize(64 * x_width, 64 * y_height);

        // Ensure index is valid (array is 0-indexed)
        int index = picture.get() - 1;
        if (index < 0 || index >= TEXTURES.length) index = 0;

        renderer.texture(TEXTURES[index], x, y, getWidth(), getHeight(), Color.WHITE);
    }
}
