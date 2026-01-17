package kawaii.addon.v2.real.mixin;

import kawaii.addon.v2.real.modules.MapCensor;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.MapRenderState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
* TODO: fix/update mixin!
*/

@Mixin(MapRenderer.class)
public class MapRendererMixin {
    @Inject(method = "draw", at = @At("HEAD"), cancellable = true)
    private void onDraw(MapRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean bool, int light, CallbackInfo ci) {
        MapCensor module = Modules.get().get(MapCensor.class);
        if (module == null || !module.isActive()) return;

        // Cancel the real map so it doesn't leak coordinates
        ci.cancel();

        // Use the vertex consumer provider to render
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(module.getTexture()));

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        // Draw the custom PNG over the map area
        vertexConsumer.vertex(matrix4f, 0, 128, -0.01f).texture(0, 1).color(255, 255, 255, 255).light(light);
        vertexConsumer.vertex(matrix4f, 128, 128, -0.01f).texture(1, 1).color(255, 255, 255, 255).light(light);
        vertexConsumer.vertex(matrix4f, 128, 0, -0.01f).texture(1, 0).color(255, 255, 255, 255).light(light);
        vertexConsumer.vertex(matrix4f, 0, 0, -0.01f).texture(0, 0).color(255, 255, 255, 255).light(light);
    }
}
