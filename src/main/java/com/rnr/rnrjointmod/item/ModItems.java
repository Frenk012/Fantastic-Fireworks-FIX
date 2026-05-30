package com.rnr.rnrjointmod.item;



import com.rnr.rnrjointmod.RnRJointMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(RnRJointMod.MOD_ID);

    public static final DeferredItem<Item> ITEM = ITEMS.registerItem("item",
            Item::new, new Item.Properties());

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }



}
