package io.lolyay.commands.manager;

import io.lolyay.commands.helpers.EditBuilder;
import io.lolyay.commands.helpers.ModalBuilder;
import io.lolyay.commands.helpers.ReplyBuilder;
import io.lolyay.utils.KVPair;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessagePollData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private final Map<String, KVPair<OptionType, Object>> options = new HashMap<>();
    private Message message;
    private Type type;
    private SlashCommandInteractionEvent event;
    private boolean wasReplied = false;

    public static CommandContext of(@NotNull SlashCommandInteractionEvent event) {
        final CommandContext context = new CommandContext();
        context.setEvent(event);
        context.setType(Type.SLASH);
        context.setMessage(null);
        for (OptionMapping optionMapping : event.getOptions()) {
            context.addOption(optionMapping.getName(), optionMappingToValue(optionMapping), optionMapping.getType());
        }
        return context;
    }

    public static CommandContext of(@NotNull Message message) {
        final CommandContext context = new CommandContext();
        context.setEvent(null);
        context.setType(Type.PREFIXED);
        context.setMessage(message);
        return context;
    }

    private static Object optionMappingToValue(OptionMapping optionMapping) {
        if (optionMapping.getType() == OptionType.STRING) {
            return optionMapping.getAsString();
        } else if (optionMapping.getType() == OptionType.INTEGER) {
            return optionMapping.getAsLong();
        } else if (optionMapping.getType() == OptionType.BOOLEAN) {
            return optionMapping.getAsBoolean();
        } else if (optionMapping.getType() == OptionType.USER) {
            return optionMapping.getAsUser();
        } else if (optionMapping.getType() == OptionType.CHANNEL) {
            return optionMapping.getAsChannel();
        } else if (optionMapping.getType() == OptionType.ROLE) {
            return optionMapping.getAsRole();
        } else if (optionMapping.getType() == OptionType.MENTIONABLE) {
            return optionMapping.getAsMentionable();
        } else if (optionMapping.getType() == OptionType.NUMBER) {
            return optionMapping.getAsDouble();
        } else if (optionMapping.getType() == OptionType.ATTACHMENT) {
            return optionMapping.getAsAttachment();
        }
        return null;
    }

    public void addOption(String key, Object value, OptionType type) {
        options.put(key, new KVPair<>(type, value));
    }

    public void addOption(CommandOption option) {
        options.put(option.name, new KVPair<>(option.type, option.value));
    }

    private void setType(Type type) {
        this.type = type;
    }

    private void setMessage(Message message) {
        this.message = message;
    }

    private void setEvent(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public Map<String, KVPair<OptionType, Object>> getOptions() {
        return options;
    }

    // PUBLIC METHODS

    public SlashCommandInteractionEvent getSlashCommandEvent() {
        return event;
    }

    public boolean isFromSlash() {
        return type == Type.SLASH;
    }

    public Member getMember() {
        if (isFromSlash()) {
            return event.getMember();
        }
        return message.getMember();
    }

    public Guild getGuild() {
        if (isFromSlash()) {
            return event.getGuild();
        }
        return message.getGuild();
    }

    @Nullable
    public CommandOption getOption(@Nonnull String name) {
        if (!isFromSlash()) {
            if (options.size() == 1) {
                return CommandOption.of(options.entrySet().iterator().next().getValue(), name);
            } else return null;
        }
        return CommandOption.of(options.get(name), name);
    }

    @Nullable
    public CommandOption getOption(@Nonnull Integer position) {
        if (isFromSlash()) {
            return null;
        }
        Map.Entry<String, KVPair<OptionType, Object>> entry = options.entrySet().stream().toList().get(position);
        return CommandOption.of(entry.getValue(), entry.getKey());
    }

    public void deferReply(boolean ephemeral) {
        if (isFromSlash()) {
            if (wasReplied) return; // Avoid deferring twice
            wasReplied = true;
            event.deferReply(ephemeral).queue();
        }
        // Deferring is not possible for prefixed commands.
    }

    // START REPLY

    public ReplyBuilder<?> reply(@NotNull MessageCreateData message) {
        if (wasReplied) {
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.getHook().sendMessage(message));
            } else {
                return new ReplyBuilder<>(this.message.getChannel().sendMessage(message));
            }
        } else {
            wasReplied = true;
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.reply(message));
            } else {
                return new ReplyBuilder<>(this.message.reply(message));
            }
        }
    }

    public ReplyBuilder<?> reply(@NotNull String message) {
        if (wasReplied) {
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.getHook().sendMessage(message));
            } else {
                return new ReplyBuilder<>(this.message.getChannel().sendMessage(message));
            }
        } else {
            wasReplied = true;
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.reply(message));
            } else {
                return new ReplyBuilder<>(this.message.reply(message));
            }
        }
    }

    public ReplyBuilder<?> replyEmbeds(@NotNull Collection<? extends MessageEmbed> embeds) {
        if (wasReplied) {
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.getHook().sendMessageEmbeds(embeds));
            } else {
                return new ReplyBuilder<>(this.message.getChannel().sendMessageEmbeds(embeds));
            }
        } else {
            wasReplied = true;
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.replyEmbeds(embeds));
            } else {
                return new ReplyBuilder<>(this.message.replyEmbeds(embeds));
            }
        }
    }

    public ReplyBuilder<?> replyComponents(@NotNull Collection<? extends LayoutComponent> components) {
        if (wasReplied) {
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.getHook().sendMessageComponents(components));
            } else {
                return new ReplyBuilder<>(this.message.getChannel().sendMessageComponents(components));
            }
        } else {
            wasReplied = true;
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.replyComponents(components));
            } else {
                return new ReplyBuilder<>(this.message.replyComponents(components));
            }
        }
    }

    public ReplyBuilder<?> replyFiles(@NotNull Collection<FileUpload> files) {
        if (wasReplied) {
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.getHook().sendFiles(files));
            } else {
                return new ReplyBuilder<>(this.message.getChannel().sendFiles(files));
            }
        } else {
            wasReplied = true;
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.replyFiles(files));
            } else {
                return new ReplyBuilder<>(this.message.replyFiles(files));
            }
        }
    }

    public ReplyBuilder<?> replyPoll(@NotNull MessagePollData poll) {
        if (wasReplied) {
            if (isFromSlash()) {
                throw new UnsupportedOperationException("Followup polls are not supported for slash commands.");
            } else {
                return new ReplyBuilder<>(this.message.getChannel().sendMessagePoll(poll));
            }
        } else {
            wasReplied = true;
            if (isFromSlash()) {
                return new ReplyBuilder<>(event.replyPoll(poll));
            } else {
                return new ReplyBuilder<>(this.message.replyPoll(poll));
            }
        }
    }

    public ModalBuilder replyModal(@NotNull Modal modal) {
        if (isFromSlash()) {
            wasReplied = true;
            return new ModalBuilder(event.replyModal(modal));
        }
        return new ModalBuilder(null);
    }

    public EditBuilder editOriginal() {
        if (isFromSlash()) {
            return new EditBuilder(event.getHook());
        }
        return new EditBuilder(null);
    }

    public enum Type {
        PREFIXED, SLASH
    }

    public static class CommandOption {
        private OptionType type;
        private Object value;
        private String name;

        public static CommandOption of(OptionMapping mapping) {
            CommandOption option = new CommandOption();
            option.type = mapping.getType();
            option.name = mapping.getName();
            option.value = optionMappingToValue(mapping);
            return option;
        }

        public static CommandOption of(KVPair<OptionType, Object> mapping, String name) {
            CommandOption option = new CommandOption();
            option.type = mapping.getKey();
            option.name = name;
            option.value = mapping.getValue();
            return option;
        }

        @Nonnull
        public Message.Attachment getAsAttachment() {
            Object obj = this.value;
            if (obj instanceof Message.Attachment) {
                return (Message.Attachment) obj;
            } else {
                throw new IllegalStateException("Cannot resolve option of type " + this.type + " to Attachment!");
            }
        }

        @Nonnull
        public String getAsString() {
            return (String) this.value;
        }

        public boolean getAsBoolean() {
            if (this.type != OptionType.BOOLEAN) {
                throw new IllegalStateException("Cannot convert option of type " + this.type + " to boolean");
            } else {
                return (Boolean) this.value;
            }
        }

        public long getAsLong() {
            return switch (this.type) {
                case STRING, MENTIONABLE, CHANNEL, ROLE, USER, INTEGER, ATTACHMENT -> (Long) this.value;
                default -> throw new IllegalStateException("Cannot convert option of type " + this.type + " to long");
            };
        }

        public int getAsInt() {
            return (int) this.getAsLong();
        }

        public double getAsDouble() {
            return (Double) this.value;
        }

        @Nonnull
        public IMentionable getAsMentionable() {
            return (IMentionable) this.value;
        }

        @Nullable
        public Member getAsMember() {
            return (Member) this.value;
        }

        @Nonnull
        public User getAsUser() {
            return (User) this.value;
        }

        @Nonnull
        public Role getAsRole() {
            return (Role) this.value;
        }

        @Nonnull
        public GuildChannelUnion getAsChannel() {
            return (GuildChannelUnion) this.value;
        }

        @Nonnull
        public ChannelType getChannelType() {
            return this.getAsChannel().getType();
        }
    }
}
