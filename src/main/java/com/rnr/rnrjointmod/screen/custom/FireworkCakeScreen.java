package com.rnr.rnrjointmod.screen.custom;

import com.rnr.rnrjointmod.Payloads.FireworkBeginingPayload;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.dataattatchments.ModDataAttatchments;
import com.rnr.rnrjointmod.particals.FireworkBall;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import com.rnr.rnrjointmod.particals.Trail;
import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.awt.*;

public class FireworkCakeScreen extends Screen {
    public FireworkCakeScreen() {
        super(Component.literal("hi"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        //this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        renderImGui();
        //super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 296){
            this.onClose();
            return true;
        }// else if (keyCode == 83 || keyCode == 65 || keyCode == 87 || keyCode == 68 || keyCode == 32 ) {
//            return true;
//        }
        return super.keyPressed(keyCode, scanCode, modifiers);

    }
    //variables
    //// Firework Beginning
    boolean useTrail1 = false;
    float[] color1 = {0, 0, 0};
    float[] color2 = {0, 0, 0};
    boolean rancolinrange = false;
    float[] trans1 = {0, 0};
    float[] scale1 = {0, 0};
    int[] lifetime1 = {0};
    float[] gravity1 = {0};
    float[] ranoffset1 = {0};
    float[] upspeed1 = {0, 0};
    //// Firework Ball
    boolean useTrail1fb = false;
    float[] color1fb = {0, 0, 0};
    float[] color2fb = {0, 0, 0};
    boolean rancolinrangefb = false;
    float[] trans1fb = {0, 0};
    float[] scale1fb = {0, 0};
    int[] lifetime1fb = {0};
    float[] gravity1fb = {0};
    float[] ranoffset1fb = {0};
    float[] range1fb = {0};
    int[] repulsion1fb = {0};
    int[] count1fb = {0};
    //// Firework Trail
    float[] color1t = {0, 0, 0};
    float[] color2t = {0, 0, 0};
    boolean rancolinranget = false;
    float[] trans1t = {0, 0};
    float[] scale1t = {0, 0};
    int[] lifetime1t = {0};
    float[] gravity1t = {0};
    float[] ranoffset1t = {0};


    Player player = Minecraft.getInstance().player;
    //System.out.println(player.getName().toString());
    BlockPos pos = player.getData(ModDataAttatchments.FBLOCK); // im not getting the data for some reason
    Level level = player.level();
    FireworkCakeEntity fireworkCakeEntity = (FireworkCakeEntity) level.getBlockEntity(pos);
    boolean ran = false;
    FireworkBeginning fireworkBeginning;
    FireworkBall fireworkBall;
    Trail trail;
    Boolean changed = false;


    //System.out.println( player.getData(ModDataAttatchments.FBLOCK));

    // launching only launches on the clientside
    //it doesent save between leaveing and rejoining even though the loading is working

    private void renderImGui(){
        try(ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx != null) {
                if (fireworkCakeEntity != null) {
                    if(fireworkCakeEntity.fireworkBeginning == null) {
                        fireworkCakeEntity.setup(level, pos);
                    }
                    if(!ran){
                        fireworkBeginning = fireworkCakeEntity.getFBegining();
                        fireworkBall = fireworkCakeEntity.getFBall();
                        trail = fireworkCakeEntity.getFTrail();
                        ran = true;
                        System.out.println("ran");
                    }

                    //variables
                    //// Firework Beginning
                    useTrail1 = fireworkBeginning.usetrail;
                    color1 = fireworkBeginning.col1.getColorComponents(new float[]{0f, 0f, 0f});
                    color2 = fireworkBeginning.col2.getColorComponents(new float[]{0f, 0f, 0f});
                    rancolinrange = fireworkBeginning.rancolinrange;
                    trans1 = new float[]{fireworkBeginning.transparancy1, fireworkBeginning.transparancy2};
                    scale1 = new float[]{fireworkBeginning.scale1, fireworkBeginning.scale2};
                    lifetime1 = new int[]{fireworkBeginning.lifetime};
                    gravity1 = new float[]{fireworkBeginning.gravity};
                    ranoffset1 = new float[]{fireworkBeginning.ranoffset};
                    upspeed1 = new float[]{fireworkBeginning.initalUpVelo, fireworkBeginning.upwardsAcceleration};
                    //// Firework Ball
                    useTrail1fb = fireworkBall.usetrail;
                    color1fb = fireworkBall.col1.getColorComponents(new float[]{0f, 0f, 0f});
                    color2fb = fireworkBall.col2.getColorComponents(new float[]{0f, 0f, 0f});
                    rancolinrangefb = fireworkBall.rancolinrange;
                    trans1fb = new float[]{fireworkBall.transparancy1, fireworkBall.transparancy2};
                    scale1fb = new float[]{fireworkBall.scale1, fireworkBall.scale2};
                    lifetime1fb = new int[]{fireworkBall.lifetime};
                    gravity1fb = new float[]{fireworkBall.gravity};
                    ranoffset1fb = new float[]{fireworkBall.ranoffset};
                    range1fb = new float[]{fireworkBall.range};
                    repulsion1fb = new int[]{fireworkBall.repulsionlength};
                    count1fb = new int[]{fireworkBall.count};
                    //// Firework Trail
                    color1t = trail.col1.getColorComponents(new float[]{0f, 0f, 0f});
                    color2t = trail.col2.getColorComponents(new float[]{0f, 0f, 0f});
                    rancolinranget = trail.rancolinrange;
                    trans1t = new float[]{trail.transparancy1, trail.transparancy2};
                    scale1t = new float[]{trail.scale1, trail.scale2};
                    lifetime1t = new int[]{trail.lifetime};
                    gravity1t = new float[]{trail.gravity};
                    ranoffset1t = new float[]{trail.ranoffset};
                    // misc
                    changed = false;

                    //ImGui.showDemoWindow();
                    //imgui stuff
                    ImGui.begin("Firework Editor");
                    //if(fireworkCakeEntity != null){
                    ImGui.textWrapped("Selected Block: " + pos.getX() + " X, " + pos.getY() + " Y, " + pos.getZ() + " Z ");
                    //}
//                    if (ImGui.button("Force Save")) {
//                        System.out.println("forcesave");
//                        fireworkCakeEntity.forcesave();
//                    }
                    if (ImGui.button("Launch")) {
                        fireworkCakeEntity.shootFirework(fireworkCakeEntity.getLevel(), fireworkCakeEntity.getBlockPos());
                    }

                    if (ImGui.collapsingHeader("Rocket Body")) {
                        if(ImGui.checkbox("Use Trail", useTrail1)){
                            useTrail1 = !useTrail1;
                            fireworkBeginning.usetrail = useTrail1;
                            changed = true;
                        }

                        if(ImGui.colorPicker3("Color1", color1)) {
                            fireworkBeginning.col1 = new Color(color1[0], color1[1], color1[2]);
                            //System.out.println(color1[0] + " " + color1[1]  + " " + color1[2]);
                            changed = true;
                        }

                        if(ImGui.colorPicker3("Color2", color2)) {
                            fireworkBeginning.col2 = new Color(color2[0], color2[1], color2[2]);
                            //System.out.println(color2[0] + " " + color2[1]  + " " + color2[2]);
                            changed = true;
                        }

                        if(ImGui.checkbox("Random Color In Range (col1 - col2)", rancolinrange)){
                            rancolinrange = !rancolinrange;
                            fireworkBeginning.rancolinrange = rancolinrange;
                            changed = true;
                        }

                        ImGui.textWrapped("Start Transparency : End Transparency");
                        if(ImGui.sliderFloat2("##0", trans1, 0f, 1f)){
                            fireworkBeginning.transparancy1 = trans1[0];
                            fireworkBeginning.transparancy2 = trans1[1];
                            changed = true;
                        }

                        ImGui.textWrapped("Start Scale : End Scale"); //could be size but i already named all the variables scale so im sticking with it
                        if(ImGui.sliderFloat2("##1", scale1, 0f, 10f)){
                            fireworkBeginning.scale1 = scale1[0];
                            fireworkBeginning.scale2 = scale1[1];
                            changed = true;
                        }

                        ImGui.textWrapped("Lifetime");
                        if(ImGui.sliderInt("##2", lifetime1, 0, 150)){
                            fireworkBeginning.lifetime =  lifetime1[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Gravity");
                        if(ImGui.sliderFloat("##3", gravity1, 0f, 10f)){
                            fireworkBeginning.gravity = gravity1[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Random Offset");
                        if(ImGui.sliderFloat("##4", ranoffset1, 0f, 2.5f)){
                            fireworkBeginning.ranoffset = ranoffset1[0];
                            changed = true;
                        }
                        ImGui.textWrapped("Up Velocity : Up Acceleration");
                        if(ImGui.sliderFloat2("##5", upspeed1, 0f, 5f)){
                            fireworkBeginning.initalUpVelo = upspeed1[0];
                            fireworkBeginning.upwardsAcceleration = upspeed1[1];
                            changed = true;
                        }


                    }


                    if (ImGui.collapsingHeader("Rocket Explosion")) {

                        if(ImGui.checkbox("Use Trail##fb", useTrail1fb)){
                            useTrail1fb = !useTrail1fb;
                            fireworkBall.usetrail = useTrail1fb;
                            changed = true;
                        }

                        if(ImGui.colorPicker3("Color1##fb", color1fb)) {
                            fireworkBall.col1 = new Color(color1fb[0], color1fb[1], color1fb[2]);
                            //System.out.println(color1[0] + " " + color1[1]  + " " + color1[2]);
                            changed = true;
                        }

                        if(ImGui.colorPicker3("Color2##fb", color2fb)) {
                            fireworkBall.col2 = new Color(color2fb[0], color2fb[1], color2fb[2]);
                            //System.out.println(color2[0] + " " + color2[1]  + " " + color2[2]);
                            changed = true;
                        }

                        if(ImGui.checkbox("Random Color In Range (col1 - col2)##fb", rancolinrangefb)){
                            rancolinrangefb = !rancolinrangefb;
                            fireworkBall.rancolinrange = rancolinrangefb;
                            changed = true;
                        }

                        ImGui.textWrapped("Start Transparency : End Transparency");
                        if(ImGui.sliderFloat2("##0fb", trans1fb, 0f, 1f)){
                            fireworkBall.transparancy1 = trans1fb[0];
                            fireworkBall.transparancy2 = trans1fb[1];
                            changed = true;
                        }

                        ImGui.textWrapped("Start Scale : End Scale"); //could be size but i already named all the variables scale so im sticking with it
                        if(ImGui.sliderFloat2("##1fb", scale1fb, 0f, 10f)){
                            fireworkBall.scale1 = scale1fb[0];
                            fireworkBall.scale2 = scale1fb[1];
                            changed = true;
                        }

                        ImGui.textWrapped("Lifetime");
                        if(ImGui.sliderInt("##2fb", lifetime1fb, 0, 150)){
                            fireworkBall.lifetime =  lifetime1fb[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Gravity");
                        if(ImGui.sliderFloat("##3fb", gravity1fb, 0f, 10f)){
                            fireworkBall.gravity = gravity1fb[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Random Offset");
                        if(ImGui.sliderFloat("##4fb", ranoffset1fb, 0f, 2.5f)){
                            fireworkBall.ranoffset = ranoffset1fb[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Repulsion Range");
                        if(ImGui.sliderFloat("##5fb", range1fb, 0f, 10f)){
                            fireworkBall.range = range1fb[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Repulsion Lifetime");
                        if(ImGui.sliderInt("##6fb", repulsion1fb, 0, 60)){
                            fireworkBall.repulsionlength = repulsion1fb[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Particle Count");
                        if(ImGui.sliderInt("##7fb", count1fb, 0, 400)){
                            fireworkBall.count = count1fb[0];
                            changed = true;
                        }
                    }


                    if (ImGui.collapsingHeader("Rocket Trail")) {
                        if(ImGui.colorPicker3("Color1##t", color1t)) {
                            trail.col1 = new Color(color1t[0], color1t[1], color1t[2]);
                            changed = true;
                        }

                        if(ImGui.colorPicker3("Color2##t", color2t)) {
                            trail.col2 = new Color(color2t[0], color2t[1], color2t[2]);
                            changed = true;
                        }

                        if(ImGui.checkbox("Random Color In Range (col1 - col2)##t", rancolinranget)){
                            rancolinranget = !rancolinranget;
                            trail.rancolinrange = rancolinranget;
                            changed = true;
                        }

                        ImGui.textWrapped("Start Transparency : End Transparency");
                        if(ImGui.sliderFloat2("##0t", trans1t, 0f, 1f)){
                            trail.transparancy1 = trans1t[0];
                            trail.transparancy2 = trans1t[1];
                            changed = true;
                        }

                        ImGui.textWrapped("Start Scale : End Scale"); //could be size but i already named all the variables scale so im sticking with it
                        if(ImGui.sliderFloat2("##1t", scale1t, 0f, 10f)){
                            trail.scale1 = scale1t[0];
                            trail.scale2 = scale1t[1];
                            changed = true;
                        }

                        ImGui.textWrapped("Lifetime");
                        if(ImGui.sliderInt("##2t", lifetime1t, 0, 150)){
                            trail.lifetime =  lifetime1t[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Gravity");
                        if(ImGui.sliderFloat("##3t", gravity1t, 0f, 10f)){
                            trail.gravity = gravity1t[0];
                            changed = true;
                        }

                        ImGui.textWrapped("Random Offset");
                        if(ImGui.sliderFloat("##4t", ranoffset1t, 0f, 2.5f)){
                            trail.ranoffset = ranoffset1t[0];
                            changed = true;
                        }
                    }
                    fireworkCakeEntity.setFBegining(fireworkBeginning);
                    fireworkCakeEntity.setFBall(fireworkBall);
                    fireworkCakeEntity.setFTrail(trail);
                    if(changed) {
                        PacketDistributor.sendToServer(new FireworkBeginingPayload.FireworkBeginingPayloadRec(level.getDescriptionKey(), pos.toShortString(), fireworkBeginning.serializeNBT(level.registryAccess()), fireworkBall.serializeNBT(level.registryAccess()), trail.serializeNBT(level.registryAccess())));
                        fireworkCakeEntity.setChanged();
                    }
                    ImGui.end();
                }


                else {
                    ImGui.begin("Firework Editor");
                    ImGui.textWrapped("Selected block is invalid try placing a firework cake block here: " + pos.getX() + " X, " + pos.getY() + " Y, " + pos.getZ() + " Z ");
                    ImGui.end();
                }
            }
        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
