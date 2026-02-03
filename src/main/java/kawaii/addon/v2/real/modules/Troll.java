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

    private final Setting<Boolean> fahhhOnDeath = sgGeneral.add(new BoolSetting.Builder()
            .name("fahhh-on-death")
            .description("Plays a custom sound when you die.")
            .defaultValue(true)
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

    private static final Identifier FAHHH_ID = Identifier.of("kawaii-addon", "fahhh_event");
    private static final SoundEvent FAHHH_SOUND = SoundEvent.of(FAHHH_ID);

    private boolean wasDead = false;

    public Troll() {
        super(KawaiiAddon.CATEGORY, "troll", "Changes some stuff. :)");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || !fahhhOnDeath.get()) return;

        boolean dead = mc.player.isDead();

        if (dead && !wasDead) {
            mc.getSoundManager().play(
                PositionedSoundInstance.master(FAHHH_SOUND, pitch.get().floatValue(), volume.get().floatValue())
            );
        }
        wasDead = dead;
    }
}
