package io.github.jhonwesleyy.securedatamasker.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleDescriptor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Verifies RNF09: only the public API packages are exported; {@code internal} is not. */
class ModuleDescriptorTest {

    private static final String BASE = "io.github.jhonwesleyy.securedatamasker.core";

    @Test
    void exportsOnlyThePublicApiPackages() throws IOException {
        ModuleDescriptor descriptor = readCompiledDescriptor();

        Set<String> exported = descriptor.exports().stream()
                .map(ModuleDescriptor.Exports::source)
                .collect(Collectors.toSet());

        assertEquals(BASE, descriptor.name());
        assertEquals(Set.of(BASE, BASE + ".validation", BASE + ".detection", BASE + ".masking"), exported);
        assertTrue(descriptor.packages().contains(BASE + ".internal"), "internal package exists");
        assertTrue(descriptor.exports().stream().noneMatch(ModuleDescriptor.Exports::isQualified));
        assertTrue(descriptor.opens().isEmpty(), "no package is opened for reflection");
    }

    @Test
    void hasNoRuntimeModuleDependencies() throws IOException {
        ModuleDescriptor descriptor = readCompiledDescriptor();

        Set<String> nonStatic = descriptor.requires().stream()
                .filter(r -> !r.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC))
                .map(ModuleDescriptor.Requires::name)
                .collect(Collectors.toSet());

        assertEquals(Set.of("java.base"), nonStatic, "RNF04: no runtime dependencies");
    }

    private static ModuleDescriptor readCompiledDescriptor() throws IOException {
        Path descriptorFile = Path.of("target", "classes", "module-info.class");
        try (InputStream in = Files.newInputStream(descriptorFile)) {
            return ModuleDescriptor.read(in, () -> packagesUnder(Path.of("target", "classes")));
        }
    }

    private static Set<String> packagesUnder(Path root) {
        try (var files = Files.walk(root)) {
            return files.filter(p -> p.toString().endsWith(".class"))
                    .map(root::relativize)
                    .map(Path::getParent)
                    .filter(p -> p != null)
                    .map(p -> p.toString().replace(p.getFileSystem().getSeparator(), "."))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new IllegalStateException("cannot list compiled packages", e);
        }
    }
}
