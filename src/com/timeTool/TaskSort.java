package com.timeTool;

import java.util.Comparator;

public class TaskSort
{
	static final public int ID_SORT = 0; 
	static final public int DESCRIPTION_SORT = 1; 
	static final public int MINUTES_SORT = 2; 
	static final public int HOURS_SORT = 3; 
	
    static final Comparator<Task> ID_ASC = new Comparator<Task>() 
    {
        public int compare(Task e1, Task e2) 
        {
            return e2.getId().compareTo(e1.getId());
        }
    };
    static final Comparator<Task> ID_DESC = new Comparator<Task>() 
    {
        public int compare(Task e1, Task e2) 
        {
            return e1.getId().compareTo(e2.getId());
        }
    };
    static final Comparator<Task> DESCRIPTION_ASC = new Comparator<Task>() 
    {
        public int compare(Task e1, Task e2) 
        {
            return e2.getDescription().compareTo(e1.getDescription());
        }
    };
    static final Comparator<Task> DESCRIPTION_DESC = new Comparator<Task>() 
    {
        public int compare(Task e1, Task e2) 
        {
            return e1.getDescription().compareTo(e2.getDescription());
        }
    };
    static final Comparator<Task> MINUTES_ASC = new Comparator<Task>() 
    {
        public int compare(Task e1, Task e2) 
        {
            return e2.getMinutes().compareTo(e1.getMinutes());
        }
    };
    static final Comparator<Task> MINUTES_DESC = new Comparator<Task>() 
    {
        public int compare(Task e1, Task e2) 
        {
            return e1.getMinutes().compareTo(e2.getMinutes());
        }
    };
    static final Comparator<Task> HOURS_ASC = new Comparator<Task>() 
    {
        public int compare(Task e1, Task e2) 
        {
            return e2.getHours().compareTo(e1.getHours());
        }
    };
    static final Comparator<Task> HOURS_DESC = new Comparator<Task>() 
    {
        public int compare(Task e1, Task e2) 
        {
            return e1.getHours().compareTo(e2.getHours());
        }
    };
}
