package net.morher.house.api.entity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import net.morher.house.api.device.DeviceId;
import net.morher.house.api.device.DeviceInfo;

public class EntityTest {

    @Test
    public void setDeviceInfoNoListener() {
        testEntity(null).setDeviceInfo(new DeviceInfo());
        // Should not throw NullPointerException
    }

    @Test
    public void setDeviceInfoWithListener() {
        EntityListener listenerMock = mock(EntityListener.class);
        TestEntity entity = testEntity(listenerMock);
        entity.setDeviceInfo(new DeviceInfo());

        ArgumentCaptor<Entity> captor = ArgumentCaptor.forClass(Entity.class);

        verify(listenerMock, times(1))
                .onEntityUpdated(captor.capture());

        assertThat(captor.getValue(), is(entity));
    }

    @Test
    public void setOptionsNoListener() {
        testEntity(null).setOptions(new EntityOptions());
        // Should not throw NullPointerException
    }

    @Test
    public void setOptionsWithListener() {
        EntityListener listenerMock = mock(EntityListener.class);
        TestEntity entity = testEntity(listenerMock);
        entity.setOptions(new EntityOptions());

        ArgumentCaptor<Entity> captor = ArgumentCaptor.forClass(Entity.class);

        verify(listenerMock, times(1))
                .onEntityUpdated(captor.capture());

        assertThat(captor.getValue(), is(entity));
    }

    private TestEntity testEntity(EntityListener listener) {
        return new TestEntity(new EntityId(new DeviceId("room", "device"), "entity"), listener);
    }

    private static class TestEntity extends ConfigurableEntity<EntityOptions> {
        public TestEntity(EntityId id, EntityListener listener) {
            super(id, listener);
        }

    }
}
