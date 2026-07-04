package com.rnr.rnrjointmod.Payloads;

import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleDataOnMain(final FireworkBeginingPayload.FireworkBeginingPayloadRec data, final IPayloadContext context) {
        // Do something with the data, on the main thread
        context.enqueueWork(() ->{
                    String strlevel = data.level();
                    Level level = context.player().level(); // prolly will bug clientside acrross demensions
//                    if(strlevel.contains("end")){
//                        level = context.player().getServer().getLevel(Level.END);
//                    }else if(strlevel.contains("nether")){
//                        level = context.player().getServer().getLevel(Level.NETHER);
//                    }

                    String[] strpos = data.blockpos().replaceAll(" ", "").split(",");
                    BlockPos pos = new BlockPos(Integer.valueOf(strpos[0]) , Integer.valueOf(strpos[1]), Integer.valueOf(strpos[2]));

                    FireworkCakeEntity fireworkCakeEntity = (FireworkCakeEntity) level.getBlockEntity(pos);
                    //System.out.println("abc" + fireworkCakeEntity);
                    if (fireworkCakeEntity != null){
                        if(fireworkCakeEntity.fireworkBeginning == null || fireworkCakeEntity.fireworkBall == null || fireworkCakeEntity.trail == null) {
                            fireworkCakeEntity.setup(level, pos);
                        }
                        fireworkCakeEntity.fireworkBeginning.deserializeNBT(context.player().registryAccess(), data.fireworkbeginning());
                        fireworkCakeEntity.fireworkBall.deserializeNBT(context.player().registryAccess(), data.fireworkball());
                        fireworkCakeEntity.trail.deserializeNBT(context.player().registryAccess(), data.trail());
//                        fireworkBeginning.rancolinrange = data.rancolinrange();
//                        fireworkBeginning.count = data.count();
//                        fireworkBeginning.transparancy1 = data.transparancy1();
//                        fireworkBeginning.transparancy2 = data.transparancy2();
//                        fireworkCakeEntity.setFBegining(fireworkBeginning);
//                        fireworkCakeEntity.setChanged();
                    }

                    System.out.println("hi im a client package");
                    //System.out.println(data.transparancy1());
                    //System.out.println(fireworkCakeEntity.fireworkBeginning.transparancy1);
                }
        );
    }
}