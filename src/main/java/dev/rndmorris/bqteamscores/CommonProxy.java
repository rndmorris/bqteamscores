package dev.rndmorris.bqteamscores;

import betterquesting.api.api.ApiReference;
import betterquesting.api.api.QuestingAPI;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import dev.rndmorris.bqteamscores.rewards.factory.FactoryRewardTeamScoreboard;
import dev.rndmorris.bqteamscores.tasks.factory.FactoryTaskTeamScoreboard;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("bq_standard")) {
            registerQuestingComponents();
        }
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}

    public void registerQuestingComponents() {
        final var taskRegistry = QuestingAPI.getAPI(ApiReference.TASK_REG);
        taskRegistry.register(FactoryTaskTeamScoreboard.INSTANCE);

        final var rewardRegistry = QuestingAPI.getAPI(ApiReference.REWARD_REG);
        rewardRegistry.register(FactoryRewardTeamScoreboard.INSTANCE);
    }
}
