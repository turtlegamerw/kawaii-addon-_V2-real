package kawaii.addon.v2.real.hud;

import kawaii.addon.v2.real.AddonTemplate;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Identifier;

public class HudCat extends HudElement {
    //TODO make it render
    public static final HudElementInfo<HudCat> INFO = new HudElementInfo<>(AddonTemplate.HUD_GROUP, "cat-hud", "Displays a cat icon.", HudCat::new);
    public static final Identifier TEXTURE = Identifier.of("kawaii-addon", "textures/hud/cat1.png");
    private final Identifier catgirl = Identifier.of("blackout", "catgirl.png");
    public HudCat() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        setSize(64, 64);
        renderer.texture(TEXTURE, x, y, getWidth(), getHeight(), Color.WHITE);
    }
}
