package dev.rndmorris.bqteamscores.tasks.factory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import betterquesting.api.questing.tasks.ITask;
import betterquesting.api2.registry.IFactoryData;
import dev.rndmorris.bqteamscores.BqTeamScoreboard;
import dev.rndmorris.bqteamscores.tasks.TaskTeamScoreboard;

public class FactoryTaskTeamScoreboard implements IFactoryData<ITask, NBTTagCompound> {

    public static final FactoryTaskTeamScoreboard INSTANCE = new FactoryTaskTeamScoreboard();

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(BqTeamScoreboard.MODID + ":teamscoreboard");
    }

    @Override
    public TaskTeamScoreboard createNew() {
        return new TaskTeamScoreboard();
    }

    @Override
    public TaskTeamScoreboard loadFromData(NBTTagCompound data) {
        final var task = new TaskTeamScoreboard();
        task.readFromNBT(data);
        return task;
    }
}
