package com.example.lab_8.utils.observer;

import com.example.lab_8.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
