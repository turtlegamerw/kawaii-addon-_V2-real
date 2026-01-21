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

        // Cancel the real map so it doesn't leak coordinates
        ci.cancel();

        //vertex stuff
        VertexConsumerProvider.Immediate consumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        //layer stuff
        RenderLayer layer = RenderLayers.text(module.getTexture());
        VertexConsumer vertexConsumer = consumers.getBuffer(layer);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        int overlay = OverlayTexture.DEFAULT_UV;
        int lightU = light & 0xFFFF;
        int lightV = (light >> 16) & 0xFFFF;

        // Draw the custom PNG over the map area
        vertexConsumer.vertex(matrix4f, 0f, 128f, -0.01f).texture(0f, 1f).overlay(overlay).light(lightU, lightV).color(255, 255, 255, 255).light(light);
        vertexConsumer.vertex(matrix4f, 128f, 128f, -0.01f).texture(1f, 1f).overlay(overlay).light(lightU, lightV).color(255, 255, 255, 255).light(light);
        vertexConsumer.vertex(matrix4f, 128f, 0f, -0.01f).texture(1f, 0f).overlay(overlay).light(lightU, lightV).color(255, 255, 255, 255).light(light);
        vertexConsumer.vertex(matrix4f, 0f, 0f, -0.01f).texture(0f, 0f).overlay(overlay).light(lightU, lightV).color(255, 255, 255, 255).light(light);
        consumers.draw();
    }
}
