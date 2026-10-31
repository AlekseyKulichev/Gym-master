package ru.yandex.practicum;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {
    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertSame(singleTrainingSession, mondaySessions.getFirst());

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertEquals(13, thursdaySessions.get(0).getTimeOfDay().getHours());
        assertEquals(20, thursdaySessions.get(1).getTimeOfDay().getHours());
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> monday1300Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, monday1300Sessions.size());
        List<TrainingSession> monday1400Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertEquals(0, monday1400Sessions.size());
    }

    @Test
    void testGetTrainingSessionsForDaySortsByMinutes() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession laterSession = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 45));
        TrainingSession earlierSession = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 15));

        timetable.addNewTrainingSession(laterSession);
        timetable.addNewTrainingSession(earlierSession);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertSame(earlierSession, sessions.get(0));
        assertSame(laterSession, sessions.get(1));
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeReturnsAllSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group childrenGroup = new Group("Акробатика для детей", Age.CHILD, 60);
        Group adultsGroup = new Group("Акробатика для взрослых", Age.ADULT, 90);

        TrainingSession childrenSession = new TrainingSession(
                childrenGroup, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession adultsSession = new TrainingSession(
                adultsGroup, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(childrenSession);
        timetable.addNewTrainingSession(adultsSession);

        List<TrainingSession> sessionsForDay =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> sessionsForTime =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertEquals(2, sessionsForDay.size());
        assertSame(childrenSession, sessionsForDay.get(0));
        assertSame(adultsSession, sessionsForDay.get(1));
        assertEquals(2, sessionsForTime.size());
        assertSame(childrenSession, sessionsForTime.get(0));
        assertSame(adultsSession, sessionsForTime.get(1));
    }

    @Test
    void testGetTrainingSessionsReturnsEmptyListsForEmptyTimetable() {
        Timetable timetable = new Timetable();

        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty());
        assertTrue(timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeDoesNotMixDaysOrTimes() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession mondaySession = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession mondayLaterSession = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        TrainingSession tuesdaySession = new TrainingSession(
                group, coach, DayOfWeek.TUESDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(mondaySession);
        timetable.addNewTrainingSession(mondayLaterSession);
        timetable.addNewTrainingSession(tuesdaySession);

        List<TrainingSession> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> mondayAtOnePm =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        List<TrainingSession> tuesdayAtOnePm =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.TUESDAY, new TimeOfDay(13, 0));

        assertEquals(2, mondaySessions.size());
        assertSame(mondaySession, mondaySessions.get(0));
        assertSame(mondayLaterSession, mondaySessions.get(1));
        assertEquals(1, mondayAtOnePm.size());
        assertSame(mondaySession, mondayAtOnePm.getFirst());
        assertEquals(1, tuesdayAtOnePm.size());
        assertSame(tuesdaySession, tuesdayAtOnePm.getFirst());
    }

    @Test
    void testTimetableRejectsNullArguments() {
        Timetable timetable = new Timetable();
        TimeOfDay timeOfDay = new TimeOfDay(13, 0);

        assertThrows(NullPointerException.class, () -> timetable.addNewTrainingSession(null));
        assertThrows(NullPointerException.class, () -> timetable.getTrainingSessionsForDay(null));
        assertThrows(NullPointerException.class,
                () -> timetable.getTrainingSessionsForDayAndTime(null, timeOfDay));
        assertThrows(NullPointerException.class,
                () -> timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, null));
    }

    @Test
    void testReturnedSessionListsAreUnmodifiable() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession session = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession anotherSession = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        timetable.addNewTrainingSession(session);

        List<TrainingSession> sessionsForDay = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> sessionsForTime = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertThrows(UnsupportedOperationException.class,
                () -> sessionsForDay.add(anotherSession));
        assertThrows(UnsupportedOperationException.class,
                () -> sessionsForTime.add(anotherSession));
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
    }

    @Test
    void testGetCounterOfTrainingsForEmptyTimetable() {
        Timetable timetable = new Timetable();

        assertEquals(0, timetable.getCountByCoaches().size());
    }

    @Test
    void testGetCounterOfTrainingsForOneCoach() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, new Coach("Васильев", "Николай", "Сергеевич"),
                DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();

        assertEquals(1, counters.size());
        assertEquals(coach, counters.getFirst().getCoach());
        assertEquals(3, counters.getFirst().getCount());
    }

    @Test
    void testGetCounterOfTrainingsSortedByCountDescending() {
        Timetable timetable = new Timetable();
        Coach coachWithMoreSessions = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachWithOneSession = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithMoreSessions, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithMoreSessions, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithMoreSessions, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithOneSession, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();

        assertEquals(2, counters.size());
        assertEquals(coachWithMoreSessions, counters.get(0).getCoach());
        assertEquals(3, counters.get(0).getCount());
        assertEquals(coachWithOneSession, counters.get(1).getCoach());
        assertEquals(1, counters.get(1).getCount());
    }
}
