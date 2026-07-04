package com.rnr.rnrjointmod.Payloads;

import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.awt.*;


public class ServerPayloadHandler {

    public static void handleDataOnMain(final FireworkBeginingPayload.FireworkBeginingPayloadRec data, final IPayloadContext context) {
        // Do something with the data, on the main thread
        context.enqueueWork(() ->{


        String strlevel = data.level();
        Level level = context.player().getServer().getLevel(Level.OVERWORLD);
        if(strlevel.contains("end")){
            level = context.player().getServer().getLevel(Level.END);
        }else if(strlevel.contains("nether")){
            level = context.player().getServer().getLevel(Level.NETHER);
        }

        String[] strpos = data.blockpos().replaceAll(" ", "").split(",");
        BlockPos pos = new BlockPos(Integer.valueOf(strpos[0]) , Integer.valueOf(strpos[1]), Integer.valueOf(strpos[2]));

        FireworkCakeEntity fireworkCakeEntity = (FireworkCakeEntity) level.getBlockEntity(pos);
        if (fireworkCakeEntity != null){
            if(fireworkCakeEntity.fireworkBeginning == null || fireworkCakeEntity.fireworkBall == null || fireworkCakeEntity.trail == null) {
                fireworkCakeEntity.setup(level, pos);
            }
            fireworkCakeEntity.fireworkBeginning.deserializeNBT(context.player().registryAccess(), data.fireworkbeginning());
            fireworkCakeEntity.fireworkBall.deserializeNBT(context.player().registryAccess(), data.fireworkball());
            fireworkCakeEntity.trail.deserializeNBT(context.player().registryAccess(), data.trail());
//            if(data.trailoff()){
//                fireworkBeginning.trail = null;
//            }
//            fireworkBeginning.rancolinrange = data.rancolinrange();
//            fireworkBeginning.count = data.count();
//            fireworkBeginning.transparancy1 = data.transparancy1();
//            fireworkBeginning.transparancy2 = data.transparancy2();
//            fireworkBeginning.scale1 = data.scale1();
//            fireworkBeginning.scale2 = data.scale2();
//            fireworkBeginning.lifetime = data.lifetime();
//            fireworkBeginning.ranoffset = data.ranoffset();
//            fireworkBeginning.initalUpVelo = data.initalupvelo();
//            fireworkBeginning.upwardsAcceleration = data.upwardsacceleration();
//
//            String col1str = data.col1();
//            col1str.replace("java.awt.Color[r=", "");
//            col1str.replace("g=", "");
//            col1str.replace("b=", "");
//            col1str.replace("]", "");
//            String[] col1strarray = col1str.split(",");
//            fireworkBeginning.col1 = new Color(Integer.getInteger(col1strarray[0]), Integer.getInteger(col1strarray[1]), Integer.getInteger(col1strarray[2]));
//            String col2str = data.col2();
//            col2str.replace("java.awt.Color[r=", "");
//            col2str.replace("g=", "");
//            col2str.replace("b=", "");
//            col2str.replace("]", "");
//            String[] col2strarray = col2str.split(",");
//            fireworkBeginning.col2 = new Color(Integer.getInteger(col2strarray[0]), Integer.getInteger(col2strarray[1]), Integer.getInteger(col2strarray[2]));



            PacketDistributor.sendToAllPlayers(new FireworkBeginingPayload.FireworkBeginingPayloadRec(level.getDescriptionKey(), pos.toShortString(), fireworkCakeEntity.fireworkBeginning.serializeNBT(context.player().registryAccess()), fireworkCakeEntity.fireworkBall.serializeNBT(context.player().registryAccess()), fireworkCakeEntity.trail.serializeNBT(context.player().registryAccess())));
            fireworkCakeEntity.setChanged();
        }

        System.out.println("hi im a server package");
        }
        );
    }
}