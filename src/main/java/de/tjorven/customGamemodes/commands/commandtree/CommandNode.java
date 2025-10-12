package de.tjorven.customGamemodes.commands.commandtree;

import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class CommandNode {

    private final String name;
    private final String usage;
    private final String description;
    private final BiConsumer<CommandSender, String[]> executor;
    private final Map<String, CommandNode> children = new LinkedHashMap<>();
    private final BiFunction<CommandSender, String[], List<String>> tabCompletionProvider;

    private CommandNode(String name, String usage, String description, BiConsumer<CommandSender, String[]> executor,
                        BiFunction<CommandSender, String[], List<String>> tabCompletionProvider) {
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.executor = executor;
        this.tabCompletionProvider = tabCompletionProvider;
    }

    public static CommandNode root(String name) {
        return new CommandNode(name, "", "", null, null);
    }

    public CommandNode sub(String name, String usage, String description, BiConsumer<CommandSender, String[]> executor,
                           BiFunction<CommandSender, String[], List<String>> tabCompletionProvider) {
        CommandNode child = new CommandNode(name.toLowerCase(), usage, description, executor, tabCompletionProvider);
        children.put(child.name, child);
        return this;
    }

    public CommandNode sub(String name, String usage, String description, BiConsumer<CommandSender, String[]> executor) {
        return sub(name, usage, description, executor, null);
    }

    public CommandNode sub(String name, String usage, String description, Consumer<CommandNode> builder) {
        CommandNode child = new CommandNode(name.toLowerCase(), usage, description, null, null);
        builder.accept(child);
        children.put(child.name, child);
        return this;
    }

    public boolean handleExecute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender, label);
            return true;
        }

        String key = args[0].toLowerCase();
        CommandNode child = children.get(key);

        if (child != null) {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            if (!child.children.isEmpty() && (subArgs.length == 0 || child.children.containsKey(subArgs[0].toLowerCase()))) {
                return child.handleExecute(sender, label + " " + child.name, subArgs);
            } else if (child.executor != null) {
                child.executor.accept(sender, subArgs);
            } else {
                child.sendUsage(sender, label + " " + child.name);
            }
            return true;
        }

        sender.sendMessage("§cUnknown subcommand. Use §e/" + label + "§c for Help.");
        return true;
    }

    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (tabCompletionProvider != null && children.isEmpty()) {
                return tabCompletionProvider.apply(sender, args);
            }
            return new ArrayList<>(children.keySet());
        }

        String currentArg = args[0].toLowerCase();

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (CommandNode child : children.values()) {
                if (child.name.startsWith(currentArg)) completions.add(child.name);
            }
            if (this.executor != null && this.tabCompletionProvider != null) {
                completions.addAll(this.tabCompletionProvider.apply(sender, args).stream().filter( name ->
                        name.toLowerCase().startsWith(currentArg)).collect(Collectors.toCollection(ArrayList::new)));
            }
            return completions.stream().distinct().sorted().toList();
        }

        CommandNode child = children.get(args[0].toLowerCase());
        if (child != null) {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            return child.handleTabComplete(sender, subArgs);
        }

        return List.of();
    }


    private void sendUsage(CommandSender sender, String label) {
        sender.sendMessage("§eUsage for §6/" + label + "§e:");
        for (CommandNode child : children.values()) {
            sender.sendMessage("§7/" + label + " " + child.name + " " + child.usage + " §8- §f" + child.description);
        }
    }
}

