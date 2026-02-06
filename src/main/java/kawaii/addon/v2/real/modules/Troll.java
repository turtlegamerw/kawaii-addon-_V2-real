package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Troll extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<DeathSound> deathSound = sgGeneral.add(
        new EnumSetting.Builder<DeathSound>()
            .name("death-sound")
            .description("funny sounds.")
            .defaultValue(DeathSound.FAHHH)
            .build()
    );

    private final Setting<Double> pitch = sgGeneral.add(new DoubleSetting.Builder()
        .name("pitch")
        .description("set pitch.")
        .defaultValue(1.0)
        .min(0.5)
        .sliderRange(0.5, 2.0)
        .decimalPlaces(1)
        .build()
    );

    private final Setting<Double> volume = sgGeneral.add(new DoubleSetting.Builder()
        .name("volume")
        .description("set volume.")
        .defaultValue(0.5)
        .min(0.1)
        .sliderRange(0.1, 1.0)
        .decimalPlaces(1)
        .build()
    );

    public enum DeathSound {
        FAHHH("kawaii-addon", "fahhh_event"),
        VINEBOOM("kawaii-addon", "vine_boom_event"),
        METAL_PIPE("kawaii-addon", "metal-pipe_drop_event"),;
        public final SoundEvent sound;
        DeathSound(String namespace, String path) {
            Identifier id = Identifier.of(namespace, path);
            this.sound = SoundEvent.of(id);
        }
    }

    private boolean wasDead = false;

    public Troll() {
        super(KawaiiAddon.CATEGORY, "troll", "Changes some stuff. :)");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        assert mc.player != null;
        boolean dead = mc.player.isDead();

        if (dead && !wasDead) {
            mc.getSoundManager().play(
                PositionedSoundInstance.master(
                    deathSound.get().sound,
                    pitch.get().floatValue(),
                    volume.get().floatValue()
                )
            );
        }
        wasDead = dead;
    }
}
