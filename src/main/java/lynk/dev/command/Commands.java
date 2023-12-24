package lynk.dev.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static lynk.dev.Playertracker.uniquePlayers;


public class Commands {
    public static Integer showPlayercount(ServerCommandSource source) {
        String message = "Unique players joined: " + uniquePlayers.size();
        source.getPlayer().sendMessage(Text.of(message), false);

        return 1;
    }
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("playertracker")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(CommandManager.literal("total")
                                .executes(context -> {
                                    return showPlayercount((ServerCommandSource) context.getSource());
                                })
                        )
        );
    }
}
