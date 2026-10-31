package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeOfDayTest {
    @Test
    void rejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(24, 0));
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(12, -1));
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(12, 60));
    }

    @Test
    void comparesHoursAndMinutesChronologically() {
        assertEquals(-1, new TimeOfDay(13, 15).compareTo(new TimeOfDay(13, 45)));
        assertEquals(1, new TimeOfDay(14, 0).compareTo(new TimeOfDay(13, 45)));
        assertEquals(0, new TimeOfDay(13, 15).compareTo(new TimeOfDay(13, 15)));
    }
}
