package kawaii.addon.v2.real.modules;

import com.mojang.blaze3d.vertex.VertexFormat;
import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import java.lang.reflect.Field;

/*
 * rendering is broken kinda only works in 1.21.8!
 * mojang removed a rendering thing.
 */

/*
public class ModuleChinaHat extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();

    private final Setting<Double> size = sg.add(new DoubleSetting.Builder()
        .name("size")
        .description("How big the hat should be")
        .defaultValue(0.6)
        .min(0.05).sliderRange(0.05, 1.0)
        .build()
    );

    private final Setting<Integer> glowAmount = sg.add(new IntSetting.Builder()
        .name("glow-amount")
        .description("render pass opacity/blur")
        .defaultValue(10)
        .min(0).max(100)
        .build()
    );

    private final Setting<Double> opacity = sg.add(new DoubleSetting.Builder()
        .name("opacity")
        .description("The opacity of the hat")
        .defaultValue(0.40)
        .min(0.0).max(1.0)
        .build()
    );

    private final Setting<Double> red = sg.add(new DoubleSetting.Builder()
        .name("red")
        .description("Red component")
        .defaultValue(1.0)
        .min(0.0).max(1.0)
        .sliderRange(0.0, 1.0)
        .build()
    );

    private final Setting<Double> green = sg.add(new DoubleSetting.Builder()
        .name("green")
        .description("Green component")
        .defaultValue(0.4)
        .min(0.0).max(1.0)
        .sliderRange(0.0, 1.0)
        .build()
    );

    private final Setting<Double> blue = sg.add(new DoubleSetting.Builder()
        .name("blue")
        .description("Blue component")
        .defaultValue(0.2)
        .min(0.0).max(1.0)
        .sliderRange(0.0, 1.0)
        .build()
    );

    //TODO fix this got removed lol
    private static final RenderLayer RENDER_LAYER = RenderLayer.getDebugFilledBox();

    public ModuleChinaHat() {
        super(KawaiiAddon.CATEGORY, "china hat", "Chinese hat.");
    }

    private static float toRadians(float deg) {
        return (float) (deg * (Math.PI / 180.0));
    }

    private static double lerp(double t, double a, double b) {
        return a + (b - a) * t;
    }

    private static double findPrevFieldValue(Object obj, String... candidateNames) {
        if (obj == null) return Double.NaN;
        Class<?> c = obj.getClass();
        for (String name : candidateNames) {
            Class<?> cur = c;
            while (cur != null) {
                try {
                    Field f = cur.getDeclaredField(name);
                    f.setAccessible(true);
                    Object val = f.get(obj);
                    if (val instanceof Number) return ((Number) val).doubleValue();
                } catch (Throwable ignored) {
                }
                cur = cur.getSuperclass();
            }
        }
        return Double.NaN;
    }

    private static double getInterpolatedCoord(Object playerObj, double current, double tickDelta, String... prevFieldNames) {
        double prev = findPrevFieldValue(playerObj, prevFieldNames);
        if (Double.isNaN(prev)) return current;
        return lerp(tickDelta, prev, current);
    }

    *
     * this is kinda broken when swimming!
     *
    @EventHandler
    private void onRender3D(Render3DEvent event) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null || mc.world == null) return;

        if (mc.options.getPerspective().isFirstPerson()) return;

        final int pointCount = 20;
        final double radius = size.get();
        final float alpha = (float) Math.max(0.0, Math.min(1.0, opacity.get()));

        double tickDelta = 0.0;
        try {
            Field td = event.getClass().getDeclaredField("tickDelta");
            td.setAccessible(true);
            tickDelta = ((Number) td.get(event)).doubleValue();
        } catch (Throwable ignored) {
            try {
                Field td2 = mc.getClass().getDeclaredField("tickDelta");
                td2.setAccessible(true);
                tickDelta = ((Number) td2.get(mc)).doubleValue();
            } catch (Throwable ignored2) {
            }
        }

        final double px = getInterpolatedCoord(mc.player, mc.player.getX(), tickDelta,
            "prevX", "lastTickPosX", "lastX", "xOld", "field_70169_q");
        final double py = getInterpolatedCoord(mc.player, mc.player.getY(), tickDelta,
            "prevY", "lastTickPosY", "lastY", "yOld", "field_70167_r");
        final double pz = getInterpolatedCoord(mc.player, mc.player.getZ(), tickDelta,
            "prevZ", "lastTickPosZ", "lastZ", "zOld", "field_70166_s");

        final double playerHeight = mc.player.getHeight();
        final Vec3d basePos = new Vec3d(px, py, pz);

        final double tipY = basePos.y + playerHeight + (0.5 * radius);

        final Camera camera = mc.gameRenderer.getCamera();
        final double camX = camera.getCameraPos().x;
        final double camY = camera.getCameraPos().y;
        final double camZ = camera.getCameraPos().z;

        MatrixStack matrices = event.matrices;

        float r = (float) Math.max(0.0, Math.min(1.0, red.get()));
        float g = (float) Math.max(0.0, Math.min(1.0, green.get()));
        float b = (float) Math.max(0.0, Math.min(1.0, blue.get()));

        matrices.push();
        matrices.translate(-camX, -camY, -camZ);

        Matrix4f model = matrices.peek().getPositionMatrix();

        Tessellator tess = Tessellator.getInstance();

        BufferBuilder buf = tess.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        for (int i = 0; i < pointCount; i++) {
            float a0 = (i / (float) pointCount) * 360f;
            float a1 = ((i + 1) / (float) pointCount) * 360f;

            float rad0 = toRadians(a0);
            float rad1 = toRadians(a1);

            double x0 = px + radius * Math.cos(rad0);
            double z0 = pz + radius * Math.sin(rad0);
            double y0 = basePos.y + playerHeight;

            double x1 = px + radius * Math.cos(rad1);
            double z1 = pz + radius * Math.sin(rad1);
            double y1 = basePos.y + playerHeight;

            buf.vertex(model, (float) (px), (float) (tipY), (float) (pz)).color(r, g, b, alpha);
            buf.vertex(model, (float) (x0), (float) (y0), (float) (z0)).color(r, g, b, alpha);
            buf.vertex(model, (float) (x1), (float) (y1), (float) (z1)).color(r, g, b, alpha);
        }

        RENDER_LAYER.draw(buf.end());

        if (glowAmount.get() > 0) {
            float glowAlpha = Math.min(1.0f, alpha * (0.02f * glowAmount.get()));
            float gr = Math.min(1f, r + 0.3f);
            float gg = Math.min(1f, g + 0.3f);
            float gb = Math.min(1f, b + 0.3f);

            buf = tess.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

            for (int i = 0; i < pointCount; i++) {
                float a0 = (i / (float) pointCount) * 360f;
                float a1 = ((i + 1) / (float) pointCount) * 360f;

                float rad0 = toRadians(a0);
                float rad1 = toRadians(a1);

                double x0 = px + radius * Math.cos(rad0);
                double z0 = pz + radius * Math.sin(rad0);
                double y0 = basePos.y + playerHeight;

                double x1 = px + radius * Math.cos(rad1);
                double z1 = pz + radius * Math.sin(rad1);
                double y1 = basePos.y + playerHeight;

                buf.vertex(model, (float) (px), (float) (tipY), (float) (pz)).color(gr, gg, gb, glowAlpha);
                buf.vertex(model, (float) (x0), (float) (y0), (float) (z0)).color(gr, gg, gb, glowAlpha);
                buf.vertex(model, (float) (x1), (float) (y1), (float) (z1)).color(gr, gg, gb, glowAlpha);
            }

            RENDER_LAYER.draw(buf.end());
        }

        matrices.pop();
    }
}

 */
