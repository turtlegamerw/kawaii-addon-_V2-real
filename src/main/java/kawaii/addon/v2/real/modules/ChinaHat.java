package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.MathHelper;

public class ChinaHat extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> size = sgGeneral.add(new DoubleSetting.Builder()
        .name("size")
        .description("How big the hat should be.")
        .defaultValue(0.6)
        .min(0.1).sliderRange(0.1, 1.0)
        .build()
    );

    private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
        .name("color")
        .description("The color of the hat.")
        .defaultValue(new SettingColor(255, 100, 50, 150))
        .build()
    );

    private final Setting<Integer> points = sgGeneral.add(new IntSetting.Builder()
        .name("points")
        .description("How smooth the hat circle is.")
        .defaultValue(30)
        .min(10).max(100)
        .build()
    );

    public ChinaHat() {
        super(KawaiiAddon.CATEGORY, "china hat", "Chinese hat.");
    }

    private static float toRadians(float deg) {
        return (float) (deg * (Math.PI / 180.0));
    }

    private static double lerp(double t, double a, double b) {
        return a + (b - a) * t;
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.options.getPerspective().isFirstPerson()) return;

        double x = MathHelper.lerp(event.tickDelta, mc.player.lastRenderX, mc.player.getX());
        double y = MathHelper.lerp(event.tickDelta, mc.player.lastRenderY, mc.player.getY());
        double z = MathHelper.lerp(event.tickDelta, mc.player.lastRenderZ, mc.player.getZ());

        double playerHeight = mc.player.getHeight();
        double radius = size.get();

        double baseHeight = y + playerHeight;
        double tipHeight = baseHeight + (radius * 0.5);

        Color c = color.get();

        for (int i = 0; i < points.get(); i++) {
            double a1 = (i / (double) points.get()) * Math.PI * 2;
            double a2 = ((i + 1) / (double) points.get()) * Math.PI * 2;

            double x1 = x + Math.cos(a1) * radius;
            double z1 = z + Math.sin(a1) * radius;
            double x2 = x + Math.cos(a2) * radius;
            double z2 = z + Math.sin(a2) * radius;

            // Top Side
            event.renderer.side(
                x, tipHeight, z,
                x1, baseHeight, z1,
                x2, baseHeight, z2,
                x, tipHeight, z,
                c, c, ShapeMode.Both
            );

            // Bottom Side
            event.renderer.side(
                x, tipHeight, z,
                x2, baseHeight, z2,
                x1, baseHeight, z1,
                x, tipHeight, z,
                c, c, ShapeMode.Both
            );
        }
    }
}
