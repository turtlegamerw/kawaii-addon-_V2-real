package kawaii.addon.v2.real.mixin;

import kawaii.addon.v2.real.modules.Cape;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends Entity {

    public AbstractClientPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void onGetSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        Cape module = Modules.get().get(Cape.class);
        if (module == null || !module.isActive()) return;
        if (!this.getUuid().equals(mc.getSession().getUuidOrNull())) return;
        SkinTextures original = cir.getReturnValue();
        if (original == null) return;
        Identifier cape = switch (module.capes.get()) {
            case cape1 -> Identifier.of("kawaii-addon", "cape/cape1.png");
            case cape2 -> Identifier.of("kawaii-addon", "cape/cape2.png");
            case cape3 -> Identifier.of("kawaii-addon", "cape/cape3.png");
            case cape4 -> Identifier.of("kawaii-addon", "cape/cape4.png");
            case hutao -> Identifier.of("kawaii-addon", "cape/hutao.png");
            case vape -> Identifier.of("kawaii-addon", "cape/vape.png");
            case RETRO -> Identifier.of("kawaii-addon", "cape/retro.png");
            default -> original.capeTexture();
        };
        SkinTextures modified = new SkinTextures(
            original.texture(), original.textureUrl(), cape, original.elytraTexture(), original.model(), original.secure()
        );
        cir.setReturnValue(modified);
    }
}
