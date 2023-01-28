package me.maki325.mc.plugins.uhc.config;

import com.google.common.base.Charsets;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class UhcConfiguration extends YamlConfiguration implements ConfigurationSerializable {

    public UhcConfig config = new UhcConfig();
    private Map<String, Object> defaultConfig = new HashMap<>();

    @Override public void loadFromString(@NotNull String contents) throws InvalidConfigurationException {
        Validate.notNull(contents, "Contents cannot be null");
        try {
            Field loaderOptionsField = YamlConfiguration.class.getDeclaredField("loaderOptions");
            loaderOptionsField.setAccessible(true);
            LoaderOptions loaderOptions = (LoaderOptions) loaderOptionsField.get(this);

            Field yamlField = YamlConfiguration.class.getDeclaredField("yaml");
            yamlField.setAccessible(true);
            Yaml yaml = (Yaml) yamlField.get(this);

            Map<String, Object> input;
            try {
                loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE); // SPIGOT-5881: Not ideal, but was default pre SnakeYAML 1.26
                input = yaml.load(contents);
            } catch (YAMLException e) {
                throw new InvalidConfigurationException(e);
            } catch (ClassCastException e) {
                throw new InvalidConfigurationException("Top level is not a Map.");
            }

            String header = parseHeader(contents);
            if (header.length() > 0) {
                options().header(header);
            }

            this.map.putAll(input);
            this.config = new UhcConfig(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotNull @Override public String saveToString() {
        try {
            Field yamlOptionsField = YamlConfiguration.class.getDeclaredField("yamlOptions");
            yamlOptionsField.setAccessible(true);
            DumperOptions yamlOptions = (DumperOptions) yamlOptionsField.get(this);

            Field yamlRepresenterField = YamlConfiguration.class.getDeclaredField("yamlRepresenter");
            yamlRepresenterField.setAccessible(true);
            Representer yamlRepresenter = (Representer) yamlRepresenterField.get(this);

            Field yamlField = YamlConfiguration.class.getDeclaredField("yaml");
            yamlField.setAccessible(true);
            Yaml yaml = (Yaml) yamlField.get(this);

            yamlOptions.setIndent(options().indent());
            yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            String header = buildHeader();
            String dump = yaml.dump(serialize());

            if (dump.equals(BLANK_CONFIG)) {
                dump = "";
            }

            return header + dump;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override public @NotNull Map<String, Object> serialize() {
        Map<String, Object> value = defaultConfig;
        value.putAll(this.config.serialize());
        return value;
    }

    public void setDefaults(final InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        this.defaultConfig = UhcConfiguration.loadConfiguration(new InputStreamReader(inputStream, Charsets.UTF_8)).map;
    }

    @NotNull public static UhcConfiguration loadConfiguration(@NotNull File file) {
        Validate.notNull(file, "File cannot be null");

        UhcConfiguration config = new UhcConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }

        return config;
    }

    @NotNull public static UhcConfiguration loadConfiguration(@NotNull Reader reader) {
        Validate.notNull(reader, "Stream cannot be null");

        UhcConfiguration config = new UhcConfiguration();

        try {
            config.load(reader);
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }

        return config;
    }
}
