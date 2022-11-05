/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.persistence.jdbc.console;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.io.console.Console;
import org.openhab.core.io.console.ConsoleCommandCompleter;
import org.openhab.core.io.console.StringsCompleter;
import org.openhab.core.io.console.extensions.AbstractConsoleCommandExtension;
import org.openhab.core.io.console.extensions.ConsoleCommandExtension;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.PersistenceServiceRegistry;
import org.openhab.persistence.jdbc.ItemTableCheckEntry;
import org.openhab.persistence.jdbc.ItemTableCheckEntryStatus;
import org.openhab.persistence.jdbc.internal.JdbcPersistenceService;
import org.openhab.persistence.jdbc.internal.JdbcPersistenceServiceConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link JdbcCommandExtension} is responsible for handling console commands
 *
 * @author Jacob Laursen - Initial contribution
 */
@NonNullByDefault
@Component(service = ConsoleCommandExtension.class)
public class JdbcCommandExtension extends AbstractConsoleCommandExtension implements ConsoleCommandCompleter {

    private static final String SUBCMD_LIST = "list";
    private static final StringsCompleter SUBCMD_COMPLETER = new StringsCompleter(List.of(SUBCMD_LIST), false);

    private final PersistenceServiceRegistry persistenceServiceRegistry;

    @Activate
    public JdbcCommandExtension(final @Reference PersistenceServiceRegistry persistenceServiceRegistry) {
        super(JdbcPersistenceServiceConstants.SERVICE_ID, "Interact with the JDBC persistence service.");
        this.persistenceServiceRegistry = persistenceServiceRegistry;
    }

    @Override
    public void execute(String[] args, Console console) {
        if (args.length < 1 || args.length > 2 || !SUBCMD_LIST.equals(args[0])) {
            printUsage(console);
            return;
        }
        for (PersistenceService persistenceService : persistenceServiceRegistry.getAll()) {
            if (persistenceService instanceof JdbcPersistenceService) {
                JdbcPersistenceService jdbcPersistenceService = (JdbcPersistenceService) persistenceService;
                listTables(jdbcPersistenceService, console, args.length == 2 && "ALL".equalsIgnoreCase(args[1]));
            }
        }
    }

    private void listTables(JdbcPersistenceService jdbcPersistenceService, Console console, Boolean all) {
        List<ItemTableCheckEntry> entries = jdbcPersistenceService.getCheckedEntries();
        if (!all) {
            entries.removeIf(t -> t.getStatus() == ItemTableCheckEntryStatus.VALID);
        }
        entries.sort(Comparator.comparing(ItemTableCheckEntry::getTableName));
        int itemNameMaxLength = entries.stream().map(t -> t.getItemName().length()).max(Integer::compare).get();
        int tableNameMaxLength = entries.stream().map(t -> t.getTableName().length()).max(Integer::compare).get();
        int statusMaxLength = Stream.of(ItemTableCheckEntryStatus.values()).map(t -> t.toString().length())
                .max(Integer::compare).get();
        console.println(String.format(
                "%1$-" + (tableNameMaxLength + 2) + "sRow Count  %2$-" + (itemNameMaxLength + 2) + "s%3$s", "Table",
                "Item", "Status"));
        console.println("-".repeat(tableNameMaxLength) + "  " + "---------  " + "-".repeat(itemNameMaxLength) + "  "
                + "-".repeat(statusMaxLength));
        for (var entry : entries) {
            console.println(String.format(
                    "%1$-" + (tableNameMaxLength + 2) + "s%2$9d  %3$-" + (itemNameMaxLength + 2) + "s%4$s",
                    entry.getTableName(), entry.getRowCount(), entry.getItemName(), entry.getStatus()));
        }
    }

    @Override
    public List<String> getUsages() {
        return Arrays.asList(buildCommandUsage(SUBCMD_LIST + " [<all>]", "list tables"));
    }

    @Override
    public @Nullable ConsoleCommandCompleter getCompleter() {
        return this;
    }

    @Override
    public boolean complete(String[] args, int cursorArgumentIndex, int cursorPosition, List<String> candidates) {
        if (cursorArgumentIndex <= 0) {
            return SUBCMD_COMPLETER.complete(args, cursorArgumentIndex, cursorPosition, candidates);
        }
        return false;
    }
}
