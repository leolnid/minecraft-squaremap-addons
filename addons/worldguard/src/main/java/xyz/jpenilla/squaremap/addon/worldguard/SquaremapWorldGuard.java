package xyz.jpenilla.squaremap.addon.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.worldguard.config.WGConfig;
import xyz.jpenilla.squaremap.addon.worldguard.hook.SquaremapHook;

import java.util.Objects;

public final class SquaremapWorldGuard extends JavaPlugin {
    private SquaremapHook squaremapHook;
    private WGConfig config;
    private StateFlag visibleFlag;
    private Flag<?> nameFlag;

    @Override
    public void onEnable() {
        this.config = new WGConfig(this);
        this.config.reload();

        this.squaremapHook = new SquaremapHook(this);
    }

    @Override
    public void onLoad() {
        this.setupFlags();
    }

    private void setupFlags() {
        final FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();
        this.visibleFlag = new StateFlag("squaremap-visible", true);

        Flag<?> nameFlag = flagRegistry.get("region-name");
        if (Objects.isNull(nameFlag)) {
            nameFlag = new StringFlag("region-name");
        }
        this.nameFlag = nameFlag;

        try {
            flagRegistry.register(this.visibleFlag);
            flagRegistry.register(this.nameFlag);
        } catch (Exception e) {
            this.getLogger().warning("Не смогли зарегистрировать флаги");
        }
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
    }

    public WGConfig config() {
        return this.config;
    }

    public StateFlag visibleFlag() {
        return this.visibleFlag;
    }
    public Flag<?> nameFlag() {
        return this.nameFlag;
    }
}
