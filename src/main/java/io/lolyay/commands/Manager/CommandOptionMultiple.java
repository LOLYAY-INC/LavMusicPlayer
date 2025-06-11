package io.lolyay.commands.Manager;


import io.lolyay.utils.KVPair;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandOptionMultiple implements CommandOptionType {
    private String name;
    private String description;
    private OptionType type;
    private final boolean required;
    private KVPair<String, String>[] options;

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> KVPair<String, String>[] enumToKVPairArray(Class<E> enumClass) {
        E[] enumValues = enumClass.getEnumConstants();
        KVPair<String, String>[] pairs = new KVPair[enumValues.length];

        for (int i = 0; i < enumValues.length; i++) {
            E enumValue = enumValues[i];
            String name = enumValue.name();
            // Convert ENUM_NAME to "Enum Name" format
            String displayName = name.charAt(0) +
                    name.substring(1).toLowerCase().replace('_', ' ');
            pairs[i] = new KVPair<>(name, displayName);
        }

        return pairs;
    }

    public CommandOptionMultiple(String name, String description, KVPair<String, String>[] options, OptionType type,boolean required) {
        this.name = name;
        this.required = required;
        this.description = description;
        this.options = options;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public KVPair<String, String>[] getOptions() {
        return options;
    }

    public void setOptions(KVPair<String, String>[] options) {
        this.options = options;
    }

    @Override
    public OptionType getType() {
        return type;
    }

    public void setType(OptionType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }
}
