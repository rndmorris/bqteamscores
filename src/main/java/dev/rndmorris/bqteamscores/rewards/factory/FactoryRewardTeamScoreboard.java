package dev.rndmorris.bqteamscores.rewards.factory;

import betterquesting.api.questing.rewards.IReward;
import betterquesting.api2.registry.IFactoryData;
import bq_standard.rewards.RewardScoreboard;
import dev.rndmorris.bqteamscores.BqTeamScoreboard;
import dev.rndmorris.bqteamscores.rewards.RewardTeamScoreboard;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class FactoryRewardTeamScoreboard implements IFactoryData<IReward, NBTTagCompound> {

    public static final FactoryRewardTeamScoreboard INSTANCE = new FactoryRewardTeamScoreboard();

    @Override
    public IReward loadFromData(NBTTagCompound data) {
        RewardScoreboard reward = new RewardScoreboard();
        reward.readFromNBT(data);
        return reward;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(BqTeamScoreboard.MODID, "teamscoreboard");
    }

    @Override
    public IReward createNew() {
        return new RewardTeamScoreboard();
    }
}
