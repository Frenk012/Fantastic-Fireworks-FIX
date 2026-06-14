package com.rnr.rnrjointmod.screen;

import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.screen.custom.FireworkCakeMenu;
import com.rnr.rnrjointmod.screen.custom.FireworkCakeScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, RnRJointMod.MOD_ID);

    public static DeferredHolder<MenuType<?>, MenuType<FireworkCakeMenu>> FIREWORK_CAKE_MENU =
            registerMenuType("firework_cake_menu", FireworkCakeMenu::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
