import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Timetable {
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    private final Map<DayOfWeek, List<TrainingSession>> sessionsByDay = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        Objects.requireNonNull(trainingSession, "Занятие не должно быть null");
        DayOfWeek dayOfWeek = Objects.requireNonNull(
                trainingSession.getDayOfWeek(), "День недели не должен быть null");
        TimeOfDay timeOfDay = Objects.requireNonNull(
                trainingSession.getTimeOfDay(), "Время занятия не должно быть null");

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule =
                timetable.computeIfAbsent(dayOfWeek, _ -> new TreeMap<>());
        List<TrainingSession> sessionsAtTime =
                daySchedule.computeIfAbsent(timeOfDay, _ -> new ArrayList<>());
        sessionsAtTime.add(trainingSession);

        List<TrainingSession> sessionsForDay =
                sessionsByDay.computeIfAbsent(dayOfWeek, _ -> new ArrayList<>());
        int low = 0;
        int high = sessionsForDay.size();
        while (low < high) {
            int middle = (low + high) >>> 1;
            if (sessionsForDay.get(middle).getTimeOfDay().compareTo(timeOfDay) <= 0) {
                low = middle + 1;
            } else {
                high = middle;
            }
        }

        sessionsForDay.add(low, trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Objects.requireNonNull(dayOfWeek, "День недели не должен быть null");

        List<TrainingSession> sessions = sessionsByDay.get(dayOfWeek);
        return sessions == null ? List.of() : Collections.unmodifiableList(sessions);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(
            DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Objects.requireNonNull(dayOfWeek, "День недели не должен быть null");
        Objects.requireNonNull(timeOfDay, "Время занятия не должно быть null");

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return List.of();
        }
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        return sessions == null ? List.of() : Collections.unmodifiableList(sessions);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, CounterOfTrainings> counterMap = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    counterMap.computeIfAbsent(coach, CounterOfTrainings::new)
                            .incrementCount();
                }
            }
        }

        List<CounterOfTrainings> counters = new ArrayList<>(counterMap.values());
        counters.sort(Comparator.comparingInt(CounterOfTrainings::getCount).reversed());
        return counters;
    }
}