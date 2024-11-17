package dev.rndmorris.bqteamscores.tasks;

import static dev.rndmorris.bqteamscores.BqTeamScoreboard.LOG;

import java.util.Map;
import java.util.UUID;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreDummyCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;

import betterquesting.api.questing.IQuest;
import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.IGuiPanel;
import betterquesting.api2.utils.ParticipantInfo;
import bq_standard.ScoreboardBQ;
import bq_standard.client.gui.editors.tasks.GuiEditTaskScoreboard;
import bq_standard.client.gui.tasks.PanelTaskScoreboard;
import bq_standard.tasks.TaskScoreboard;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.rndmorris.bqteamscores.tasks.factory.FactoryTaskTeamScoreboard;

public class TaskTeamScoreboard extends TaskScoreboard {

    @Override
    public String getUnlocalisedName() {
        return "bqteamscores.task.teamscoreboard";
    }

    @Override
    public ResourceLocation getFactoryID() {
        return FactoryTaskTeamScoreboard.INSTANCE.getRegistryName();
    }

    @Override
    public void detect(ParticipantInfo participant, Map.Entry<UUID, IQuest> quest) {
        final var teamScore = getTeamScoreForPlayer(participant.PLAYER);
        ScoreboardBQ.INSTANCE.setScore(participant.UUID, scoreName, teamScore);
        if (operation.checkValues(teamScore, target)) {
            setComplete(participant.UUID);
            participant.markDirty(quest.getKey());
        }
    }

    public int getTeamScoreForPlayer(EntityPlayer player) {
        final var scoreboard = player.getWorldScoreboard();
        if (scoreboard == null) {
            return 0;
        }
        final var objective = getObjective(scoreboard);
        if (objective == null) {
            return 0;
        }
        final var playerTeam = player.getTeam();
        if (playerTeam == null) {
            // Player is not on a team, so just return the player's score
            return scoreboard.func_96529_a(player.getCommandSenderName(), objective)
                .getScorePoints();
        }

        final var scorePlayerTeam = scoreboard.getTeam(playerTeam.getRegisteredName());
        if (scorePlayerTeam == null) {
            return scoreboard.func_96529_a(player.getCommandSenderName(), objective)
                .getScorePoints();
        }

        return scorePlayerTeam.getMembershipCollection()
            .stream()
            .mapToInt(
                name -> scoreboard.func_96529_a(name, objective)
                    .getScorePoints())
            .sum();
    }

    private ScoreObjective getObjective(Scoreboard scoreboard) {
        var objective = scoreboard.getObjective(scoreName);
        if (objective == null) {
            try {
                var criteria = IScoreObjectiveCriteria.field_96643_a.get(type);
                if (criteria == null) {
                    criteria = new ScoreDummyCriteria(scoreName);
                }
                objective = scoreboard.addScoreObjective(scoreName, criteria);
                objective.setDisplayName(scoreDisp);
            } catch (Exception ex) {
                LOG.catching(ex);
                return null;
            }
        }
        return objective;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IGuiPanel getTaskGui(IGuiRect rect, Map.Entry<UUID, IQuest> quest) {
        return new PanelTaskScoreboard(rect, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getTaskEditor(GuiScreen parent, Map.Entry<UUID, IQuest> quest) {
        return new GuiEditTaskScoreboard(parent, quest, this);
    }

}
