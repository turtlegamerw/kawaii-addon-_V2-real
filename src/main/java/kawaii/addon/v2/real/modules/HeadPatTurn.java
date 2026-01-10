package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;

public class HeadPatTurn extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();
    //client side makes me dizzy :(
    private final Setting<SpinMode> mode = sg.add(new EnumSetting.Builder<SpinMode>()
        .name("mode")
        .description("How the rotation is applied.")
        .defaultValue(SpinMode.Server)
        .build()
    );
    private final Setting<Integer> speed = sg.add(new IntSetting.Builder()
        .name("speed")
        .description("Spin speed.")
        .defaultValue(10)
        .min(1)
        .sliderMax(100)
        .build()
    );

    private float yaw;

    public HeadPatTurn() {
        super(KawaiiAddon.CATEGORY, "HeadPatTurn", "Spins your player endlessly.");
    }

    @Override
    public void onActivate() {
        if (mc.player != null) {
            yaw = mc.player.getYaw();
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (!Utils.canUpdate()) return;

        yaw += speed.get();

        switch (mode.get()) {
            case Client -> mc.player.setYaw(yaw);
            case Server -> Rotations.rotate(yaw, 0,  -15);
        }
    }

    public enum SpinMode {
        Client,
        Server
    }
}
