package kawaii.addon.v2.real.mixin;

import kawaii.addon.v2.real.modules.MapCensor;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.MapRenderState;
import net.minecraft.client.render.OverlayTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapRenderer.class)
public class MapRendererMixin {
    @Inject(method = "draw", at = @At("HEAD"), cancellable = true)
    private void onDraw(MapRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, boolean renderDecorations, int light, CallbackInfo ci) {
        MapCensor module = Modules.get().get(MapCensor.class);
        if (module == null || !module.isActive()) return;

        ci.cancel();

        VertexConsumerProvider.Immediate consumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        RenderLayer layer = RenderLayer.getText(module.getTexture());

        VertexConsumer vertexConsumer = consumers.getBuffer(layer);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        int overlay = OverlayTexture.DEFAULT_UV;

        drawVertex(vertexConsumer, matrix4f, 0f, 128f, 0f, 1f, light, overlay);
        drawVertex(vertexConsumer, matrix4f, 128f, 128f, 1f, 1f, light, overlay);
        drawVertex(vertexConsumer, matrix4f, 128f, 0f, 1f, 0f, light, overlay);
        drawVertex(vertexConsumer, matrix4f, 0f, 0f, 0f, 0f, light, overlay);
        consumers.draw();
    }
    private void drawVertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, float u, float v, int light, int overlay) {
        buffer.vertex(matrix, x, y, -0.01f)
            .color(255, 255, 255, 255)
            .texture(u, v)
            .overlay(overlay)
            .light(light)
            .normal(0f, 0f, 1f);
    }
}
