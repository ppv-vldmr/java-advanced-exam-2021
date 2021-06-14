import core.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.*;

public class Client {
    private final static List<String> AVAILABLE_CORE_OPERATIONS = List.of(
            "add <row>", "remove <row>", "removeByIndex <index>", "list",
            "contains <row_1 row_2 ... row_n>", "queries"
    );

    public static void main(final String[] args) throws RemoteException {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("localhost", Server.DEFAULT_PORT);
        } catch (RemoteException e) {
            err.println("Can not get registry: " + e.getMessage());
            return;
        }

        Core core;
        try {
            core = (Core) registry.lookup("//localhost/core");
        } catch (final NotBoundException e) {
            err.println("core.Core is not bound");
            return;
        }

        out.println("Available core operations:" + joinWithLineSeparator(AVAILABLE_CORE_OPERATIONS));

        try (var reader = new BufferedReader(new InputStreamReader(in))) {
            String query;
            while ((query = reader.readLine()) != null) {
                String[] parts = query.split("\\s+");
                try {
                    switch (parts[0]) {
                        case "add" -> {
                            core.add(parts[1]);
                            out.println("Added row: " + parts[1]);
                        }
                        case "remove" -> {
                            int occurrences = core.remove(parts[1]);
                            out.printf("Removed %d occurrences of %s%n", occurrences, parts[1]);
                        }
                        case "removeByIndex" -> {
                            int index = Integer.parseInt(parts[1]);
                            core.removeByIndex(index);
                            out.println("Row by index " + index + " was removed");
                        }
                        case "list" -> out.print("list: " + joinWithLineSeparator(core.list()));
                        case "contains" -> {
                            List<String> queryData = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                            out.print("contains:" +
                                    joinWithLineSeparator(IntStream.range(0, queryData.size())
                                            .mapToObj(i -> parts[i + 1] + " - " + queryData.get(i))
                                            .collect(Collectors.toList())
                                    )
                            );
                        }
                        case "queries" -> out.println("queries: " + joinWithLineSeparator(core.queries()));
                        default -> err.println("Incorrect query");
                    }
                } catch (Exception e) {
                    err.println("Error occurred during query: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> String joinWithLineSeparator(List<T> list) {
        return list.stream().map(Objects::toString)
                .collect(Collectors.joining(lineSeparator(), lineSeparator(), lineSeparator()));
    }
}
