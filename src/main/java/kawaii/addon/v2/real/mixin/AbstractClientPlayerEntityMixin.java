package kawaii.addon.v2.real.mixin;

import kawaii.addon.v2.real.modules.Cape;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static meteordevelopment.meteorclient.MeteorClient.mc;

/*
 * TODO: fix injection at run time
*/

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

        AssetInfo.TextureAsset capeAsset = switch (module.capes.get()) {
            case cape1 -> new AssetInfo.TextureAssetInfo(Identifier.of("kawaii-addon", "cape/cape1.png"), null);
            case cape2 -> new AssetInfo.TextureAssetInfo(Identifier.of("kawaii-addon", "cape/cape2.png"), null);
            case cape3 -> new AssetInfo.TextureAssetInfo(Identifier.of("kawaii-addon", "cape/cape3.png"), null);
            case cape4 -> new AssetInfo.TextureAssetInfo(Identifier.of("kawaii-addon", "cape/cape4.png"), null);
            case hutao -> new AssetInfo.TextureAssetInfo(Identifier.of("kawaii-addon", "cape/hutao.png"), null);
            case vape -> new AssetInfo.TextureAssetInfo(Identifier.of("kawaii-addon", "cape/vape.png"), null);
            default -> original.cape();
        };

        SkinTextures modified = new SkinTextures(
            original.body(),
            capeAsset,
            original.elytra(),
            original.model(),
            original.secure()
        );
        cir.setReturnValue(modified);
    }
}
