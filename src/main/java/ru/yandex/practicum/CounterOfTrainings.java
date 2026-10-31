package ru.yandex.practicum;

public class CounterOfTrainings {
    private final Coach coach;
    private int count;

    public CounterOfTrainings(Coach coach) {
        this.coach = coach;
        this.count = 0;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }
}
