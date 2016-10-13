package io.github.kernegal.spongyisland.commands;

import com.flowpowered.math.vector.Vector2i;
import io.github.kernegal.spongyisland.DataHolder;
import io.github.kernegal.spongyisland.utils.IslandManager;
import io.github.kernegal.spongyisland.utils.IslandPlayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by kernegal on 12/10/2016.
 */
public class IsSetHome implements CommandExecutor {
    private DataHolder data;
    //private IslandManager isManager;
    private int islandRadius,protectionRadius;

    public IsSetHome(DataHolder data, int islandRadius, int protectionRadius) {
        this.data = data;
        //this.isManager = isManager;
        this.islandRadius = islandRadius;
        this.protectionRadius = protectionRadius;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(Text.of(TextColors.RED, "Player only."));
            return CommandResult.success();
        }
        Player player = (Player) source;

        IslandPlayer playerData = data.getPlayerData(player.getUniqueId());
        Vector2i islandCoordinates=playerData.getIsPosition().mul(islandRadius*2);

        Location<World> location = player.getLocation();

        Vector2i min = islandCoordinates.sub(protectionRadius,protectionRadius);
        Vector2i max = islandCoordinates.add(protectionRadius,protectionRadius);

        if(!location.getExtent().getName().equals("world") ||
                location.getX()<min.getX() || location.getX()>=max.getX() ||
                location.getZ()<min.getY() || location.getZ()>=max.getY()){
            player.sendMessage(Text.of(TextColors.DARK_RED,"You need to be inside of your island"));
            return CommandResult.success();
        }

        data.updateIslandHome(player.getUniqueId(),location.getPosition().toInt());
        player.sendMessage(Text.of(TextColors.GREEN,"Island home updated!"));

        return CommandResult.success();
    }
}
