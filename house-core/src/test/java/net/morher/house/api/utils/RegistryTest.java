package net.morher.house.api.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.function.Function;

import org.junit.Test;

public class RegistryTest {

    @SuppressWarnings("unchecked")
    private final Function<String, Integer> factory = mock(Function.class);
    private final Registry<String, Integer> registry = new Registry<>(factory);

    @Test
    @SuppressWarnings("unchecked")
    public void testGet() {
        doReturn(1).when(factory).apply(eq("1"));
        assertThat(registry.get("1"), is(equalTo(1)));
        verify(factory).apply(eq("1"));
        verifyNoMoreInteractions(factory);
        clearInvocations(factory);

        assertThat(registry.get("1"), is(equalTo(1)));
        verifyNoMoreInteractions(factory);

        doReturn(2).when(factory).apply(eq("2"));
        assertThat(registry.get("2"), is(equalTo(2)));
        verify(factory).apply(eq("2"));
        verifyNoMoreInteractions(factory);
        clearInvocations(factory);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHasItem() {
        assertThat(registry.hasItem("1"), is(false));

        doReturn(1).when(factory).apply(eq("1"));
        assertThat(registry.get("1"), is(equalTo(1)));
        clearInvocations(factory);

        assertThat(registry.hasItem("1"), is(true));
        assertThat(registry.hasItem("2"), is(false));
        verifyNoMoreInteractions(factory);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testItems() {
        assertThat(registry.items(), is(empty()));
        verifyNoMoreInteractions(factory);

        doReturn(1).when(factory).apply(eq("1"));
        assertThat(registry.get("1"), is(equalTo(1)));
        clearInvocations(factory);

        assertThat(registry.items(), hasItems(1));
        verifyNoMoreInteractions(factory);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemove() {
        doReturn(1).when(factory).apply(eq("1"));
        assertThat(registry.get("1"), is(equalTo(1)));
        clearInvocations(factory);

        assertThat(registry.hasItem("1"), is(true));
        verifyNoMoreInteractions(factory);

        registry.remove("1");
        assertThat(registry.hasItem("1"), is(false));
        verifyNoMoreInteractions(factory);
    }
}
