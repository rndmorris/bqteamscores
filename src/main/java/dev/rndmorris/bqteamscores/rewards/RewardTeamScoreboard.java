package dev.rndmorris.bqteamscores.rewards;

import betterquesting.api.questing.IQuest;
import bq_standard.rewards.RewardScoreboard;
import dev.rndmorris.bqteamscores.BqTeamScoreboard;
import dev.rndmorris.bqteamscores.rewards.factory.FactoryRewardTeamScoreboard;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreDummyCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.UUID;

import static dev.rndmorris.bqteamscores.BqTeamScoreboard.LOG;

public class RewardTeamScoreboard extends RewardScoreboard {

    @Override
    public ResourceLocation getFactoryID() {
        return FactoryRewardTeamScoreboard.INSTANCE.getRegistryName();
    }

    @Override
    public String getUnlocalisedName() {
        return "bqteamscores.reward.teamscoreboard";
    }

    @Override
    public void claimReward(EntityPlayer player, Map.Entry<UUID, IQuest> quest) {
        Scoreboard scoreboard = player.getWorldScoreboard();
        if (scoreboard == null) return;

        final var objective = getObjective(scoreboard);

        if (objective == null || objective.getCriteria()
            .isReadOnly()) {
            return;
        }

        final var score = scoreboard.func_96529_a(getScoreTargetName(player), objective);

        if (relative) {
            score.increseScore(value);
        } else {
            score.setScorePoints(value);
        }
    }

    private ScoreObjective getObjective(Scoreboard scoreboard) {
        var objective = scoreboard.getObjective(score);
        if (objective == null) {
            try {
                var criteria = IScoreObjectiveCriteria.field_96643_a.get(type);
                if (criteria == null) {
                    criteria = new ScoreDummyCriteria(score);
                }
                objective = scoreboard.addScoreObjective(score, criteria);
                objective.setDisplayName(score);
            } catch (Exception ex) {
                LOG.catching(ex);
                return null;
            }
        }
        return objective;
    }

    private String getScoreTargetName(EntityPlayer player) {
        final var playerTeam = player.getTeam();
        if (playerTeam != null) {
            final var fakePlayerName = BqTeamScoreboard.MODID + ":" + playerTeam.getRegisteredName();
            final var scoreboard = player.getWorldScoreboard();
            if (!playerTeam.isSameTeam(scoreboard.getPlayersTeam(fakePlayerName))) {
                scoreboard.func_151392_a(fakePlayerName, playerTeam.getRegisteredName());
            }
            return fakePlayerName;
        }
        return player.getCommandSenderName();
    }

}
