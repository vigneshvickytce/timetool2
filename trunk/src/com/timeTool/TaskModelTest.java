package com.timeTool;

import junit.framework.TestCase;

public final class TaskModelTest extends TestCase {
    private TaskModel model;
    private Task first;
    private Task second;
    private Task third;


    protected void setUp() throws Exception {
        super.setUp();

        model = new TaskModel();
        first = new Task("1", "1", 300);
        second = new Task("2", "2", 200);
        third = new Task("3", "3", 100);

        model.add(first);
        model.add(second);
        model.add(third);
    }

    public void testIDSort() {
        //descending
        model.sort(TaskSort.ID_SORT);
        assertEquals(first, model.get(0));
        assertEquals(second, model.get(1));
        assertEquals(third, model.get(2));

        //ascending
        model.sort(TaskSort.ID_SORT);
        assertEquals(third, model.get(0));
        assertEquals(second, model.get(1));
        assertEquals(first, model.get(2));

    }

    public void testDescriptionSort() {
        //descending
        model.sort(TaskSort.DESCRIPTION_SORT);
        assertEquals(third, model.get(0));
        assertEquals(second, model.get(1));
        assertEquals(first, model.get(2));

        //ascending
        model.sort(TaskSort.DESCRIPTION_SORT);
        assertEquals(first, model.get(0));
        assertEquals(second, model.get(1));
        assertEquals(third, model.get(2));
    }
    
    public void testMinutesSort() {
        //descending
        model.sort(TaskSort.MINUTES_SORT);
        assertEquals(first, model.get(0));
        assertEquals(second, model.get(1));
        assertEquals(third, model.get(2));

        //ascending
        model.sort(TaskSort.MINUTES_SORT);
        assertEquals(third, model.get(0));
        assertEquals(second, model.get(1));
        assertEquals(first, model.get(2));
    }
    public void testHoursSort() {
        //descending
        model.sort(TaskSort.HOURS_SORT);
        assertEquals(first, model.get(0));
        assertEquals(second, model.get(1));
        assertEquals(third, model.get(2));

        //ascending
        model.sort(TaskSort.HOURS_SORT);
        assertEquals(third, model.get(0));
        assertEquals(second, model.get(1));
        assertEquals(first, model.get(2));
    }
}
